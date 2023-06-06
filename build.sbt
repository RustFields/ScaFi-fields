ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = (project in file("."))
  .settings(
    name := "ScaFi-fields",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.16" % "test",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.9.0"
  )
