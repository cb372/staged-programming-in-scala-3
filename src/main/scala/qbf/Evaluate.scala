package qbf

import QBF.*

/**
 * A single-stage implementation of an interpreter to check the validity of a
 * Quantified Boolean Formula.
 *
 * Note how the traversal of the data structure and evaluation of the boolean
 * logic is interleaved.
 */
def eval(qbf: QBF, env: Map[String, Boolean]): Boolean =
  qbf match {
    case Var(name)           => env(name)
    case And(a, b)           => eval(a, env) && eval(b, env)
    case Or(a, b)            => eval(a, env) || eval(b, env)
    case Not(a)              => !(eval(a, env))
    case Implies(ante, cons) => eval(Or(cons, And(Not(ante), Not(cons))), env)
    case Forall(name, a)     =>
      // Because the `check` function is called twice, the same data structure
      // is traversed twice
      def check(value: Boolean) = eval(a, env + (name -> value))
      check(true) && check(false)
    case Exists(name, a)     =>
      def check(value: Boolean) = eval(a, env + (name -> value))
      check(true) || check(false)
  }

def evaluate(qbf: QBF): Boolean = eval(qbf, Map.empty)

import scala.quoted.*

/**
 * An equivalent staged implementation.
 *
 * This time we first traverse the data structure to generate the code, and
 * evaluation of the boolean logic does not occur until the resulting code is
 * run.
 */
def evalStaged(qbf: QBF, env: Map[String, Expr[Boolean]])(using Quotes): Expr[Boolean] =
  qbf match {
    case Var(name)           => env(name)
    case And(a, b)           => '{ ${evalStaged(a, env)} && ${evalStaged(b, env)} }
    case Or(a, b)            => '{ ${evalStaged(a, env)} || ${evalStaged(b, env)} }
    case Not(a)              => '{ ! ${evalStaged(a, env)} }
    case Implies(ante, cons) => evalStaged(Or(cons, And(Not(ante), Not(cons))), env)
    case Forall(name, a)     => '{
      // Here the recursive call to `evalStaged` is spliced, so it occurs in
      // the first stage. That means the data structure is traversed only once,
      // to generate the body of the `check` function.
      def check(value: Boolean) = ${evalStaged(a, env + (name -> 'value))}
      check(true) && check(false)
    }
    case Exists(name, a)     => '{
      def check(value: Boolean) = ${evalStaged(a, env + (name -> 'value))}
      check(true) || check(false)
    }
  }

import scala.quoted.staging.*

def evaluateStaged(qbf: QBF): Boolean =
  given Compiler = Compiler.make(getClass.getClassLoader)

  run {
    val code = evalStaged(qbf, Map.empty)
    println("Staged code: " + code.show)
    code
  }
