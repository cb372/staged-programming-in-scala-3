scalaVersion := "3.1.1"
libraryDependencies ++= Seq(
  "org.scala-lang" %% "scala3-staging" % scalaVersion.value,
  "org.typelevel" %% "cats-parse" % "0.3.6",
  "org.scalameta" %% "munit" % "1.0.0-M1" % Test
)
