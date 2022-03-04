package qbf

import munit.*

import QBF.*

class ParserTest extends FunSuite {

  val x = Var("x")
  val y = Var("y")

  test("x") {
    assertEquals(
      unsafeParse("x"), x)
  }

  test("x and y") {
    assertEquals(
      unsafeParse("x and y"),
      And(x, y)
    )
  }

  test("x or y") {
    assertEquals(
      unsafeParse("x or y"),
      Or(x, y)
    )
  }

  test("!x") {
    assertEquals(
      unsafeParse("!x"),
      Not(x)
    )
  }

  test("x => x") {
    assertEquals(
      unsafeParse("x => x"),
      Implies(x, x)
    )
  }

  test("forall x. x") {
    assertEquals(
      unsafeParse("forall x. x"),
      Forall("x", x)
    )
  }

  test("exists x. x") {
    assertEquals(
      unsafeParse("exists x. x"),
      Exists("x", x)
    )
  }

  test("x and y or y and x") {
    assertEquals(
      unsafeParse("x and y or y and x"),
      Or(And(x, y), And(y, x))
    )
  }

  test("x or y and y or x") {
    assertEquals(
      unsafeParse("x or y and y or x"),
      Or(Or(x, And(y, y)), x)
    )
  }

  test("(x or y) and (y or x)") {
    assertEquals(
      unsafeParse("(x or y) and (y or x)"),
      And(Or(x, y), Or(y, x))
    )
  }

  test("!x and y") {
    assertEquals(
      unsafeParse("!x and y"),
      And(Not(x), y)
    )
  }

  test("!(x and y)") {
    assertEquals(
      unsafeParse("!(x and y)"),
      Not(And(x, y))
    )
  }

  test("x => !x") {
    assertEquals(
      unsafeParse("x => !x"),
      Implies(x, Not(x))
    )
  }

  test("!x => x") {
    assertEquals(
      unsafeParse("!x => x"),
      Implies(Not(x), x)
    )
  }

  test("!(x => x)") {
    assertEquals(
      unsafeParse("!(x => x)"),
      Not(Implies(x, x))
    )
  }

  test("forall x. exists y. y and x => (x or !x)") {
    assertEquals(
      unsafeParse("forall x. exists y. y and x => (x or !x)"),
      Forall("x", Exists("y", And(y, Implies(x, Or(x, Not(x))))))
    )
  }

}
