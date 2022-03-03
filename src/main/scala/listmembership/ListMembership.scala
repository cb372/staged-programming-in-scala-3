package listmembership

import scala.quoted.*

/*
 * Single-stage (i.e. plain old Scala) implementation of list membership function
 */
def member[A](list: List[A])(a: A): Boolean =
  list match {
    case Nil     => false
    case x :: xs => (a == x) || member(xs)(a)
  }

/*
 * Equivalent staged implementation.
 * It takes the list in the first stage, and the element to find in the second stage.
 */
def memberStaged[A: Type: ToExpr](list: List[A])(a: Expr[A])(using Quotes): Expr[Boolean] =
  list match {
    case Nil     => '{ false }
    case x :: xs => '{ ($a == ${Expr(x)}) || ${memberStaged(xs)(a)} }
  }

/*
 * A slightly different way of writing the same thing.
 *
 * We have to use Expr.betaReduce to simplify e.g.
 * `((x: String) => x == "foo"))(a)`
 * to
 * `a == "foo"`
 * to avoid generating an unnecessarily complex expression.
 */
def memberStagedLambda[A: Type: ToExpr](list: List[A])(using Quotes): Expr[A => Boolean] = '{
  (a: A) =>
    ${
      list match {
        case Nil => '{ false }
        case x::xs => '{ (a == ${Expr(x)}) || ${Expr.betaReduce('{${memberStagedLambda(xs)}(a)})} }
      }
    }
}

import scala.quoted.staging.*

/*
 * Stage the computation by passing it a list whose contents we want to check.
 *
 * We have to use concrete String type here because we can't get hold of a Type[A] without a Quotes,
 * and Quotes is only avaiable inside `run`.
 */
def stage(list: List[String]): String => Boolean =
  given Compiler = Compiler.make(getClass.getClassLoader)

  run {
    // Here we wrap the function in a lambda, in effect partially applying the function
    // and turning `Expr[String] => Expr[Boolean]` into `Expr[String => Boolean]`
    val code: Expr[String => Boolean] = '{ (x: String) => ${memberStaged(list)('x)} }
    println("Staged code: " + code.show)
    code
  }

def stageLambda(list: List[String]): String => Boolean =
  given Compiler = Compiler.make(getClass.getClassLoader)

  run {
    // Here there's no need to do any wrapping, because it's already `Expr[String => Boolean]`
    val code: Expr[String => Boolean] = memberStagedLambda[String](list)
    println("Staged code: " + code.show)
    code
  }
