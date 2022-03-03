package qbf

import QBF.*

@main def example =
  val qbf1 = Exists("x", Forall("y", And(Or(Var("x"), Not(Var("y"))), Or(Not(Var("x")), Var("y")))))
  println(evaluate(qbf1))
  println(evaluateStaged(qbf1))

  val qbf2 = Forall("y", Exists("x", And(Or(Var("x"), Not(Var("y"))), Or(Not(Var("x")), Var("y")))))
  println(evaluate(qbf2))
  println(evaluateStaged(qbf2))

  val qbf3 = Forall("x", Or(Var("x"), Implies(Var("x"), Or(Var("x"), Not(Var("x"))))))
  println(evaluate(qbf3))
  println(evaluateStaged(qbf3))
