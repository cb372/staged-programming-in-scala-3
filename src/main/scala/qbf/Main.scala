package qbf

import QBF.*

@main def example =
  val qbf1: QBF = unsafeParse("exists x. forall y. (x or !y) and (!x or y)")
  println(evaluate(qbf1))
  println(evaluateStaged(qbf1))

  val qbf2: QBF = unsafeParse("forall y. exists x. (x or !y) and (!x or y)")
  println(evaluate(qbf2))
  println(evaluateStaged(qbf2))

  val qbf3: QBF = unsafeParse("forall x. x or (x => (x or !x))")
  println(evaluate(qbf3))
  println(evaluateStaged(qbf3))
