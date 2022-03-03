package factorial

@main def examples =
  // we can run the factorial macro using a literal integer
  println(factorial(5))

  // but we can't use anything more complicated, e.g. user input
  //println(factorial(scala.io.StdIn.readInt()))

  // for that we need runtime staging:
  println(runFactorialStaged("5".toInt))
