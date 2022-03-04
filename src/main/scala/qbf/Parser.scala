package qbf

import cats.parse.{Parser => P, _}
import cats.parse.Rfc5234.{sp, alpha}

import QBF.*

def unsafeParse(input: String): QBF = Parsers.expr.parseAll(input).right.get

/*
 * This QBF parser is not used in the talk. I just added it for fun.
 */
object Parsers {
  val dotSpace = (P.char('.') ~ sp.rep0).void

  val identifier = alpha.string.map(Var.apply)

  val expr: P[QBF] = P.defer(or)

  val parens: P[QBF] = P.defer(expr.between(P.char('('), P.char(')')))

  val value: P[QBF] = P.defer(exists | forall | not | identifier | parens)

  val not = P.defer((P.char('!') *> value).map(Not.apply))

  val exists: P[QBF] = (P.string("exists ") *> alpha.string ~ (dotSpace *> expr))
    .map { (name, a) => Exists(name, a) }

  val forall: P[QBF] = P.defer {
    (P.string("forall ") *> alpha.string ~ (dotSpace *> expr))
      .map { (name, a) => Forall(name, a) }
  }

  val implies: P[QBF] = P.defer {
    ((value <* P.string(" => ").void) ~ value)
      .map(Implies.apply)
  }

  val or: P[QBF] = P.defer {
    and.repSep(1, P.string(" or "))
      .map(nel => nel.reduceLeft(Or.apply))
  }

  val and: P[QBF] = P.defer {
    P.oneOf(implies.backtrack :: value :: Nil).repSep(1, P.string(" and "))
      .map(nel => nel.reduceLeft(And.apply))
  }
}
