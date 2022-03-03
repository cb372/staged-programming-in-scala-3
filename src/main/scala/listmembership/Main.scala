package listmembership

import scala.quoted.*

@main def examples =
  println(member(List("foo", "bar", "baz"))("wow"))
  println(member(List("foo", "bar", "baz"))("bar"))

  val staged: String => Boolean = stage(List("foo", "bar", "baz"))
  println(staged("wow"))
  println(staged("bar"))

  val staged2: String => Boolean = stageLambda(List("foo", "bar", "baz"))
  println(staged2("wow"))
  println(staged2("bar"))
