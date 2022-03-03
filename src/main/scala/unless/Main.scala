package unless

@main def example =
  unless(1 == 1){
    println("this won't be printed")
  }
  unless(1 == 2){
    println("this will be printed")
  }
