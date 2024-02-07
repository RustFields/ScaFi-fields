ThisBuild / version := "4.0.0"
ThisBuild / scalaVersion := "3.3.0"
ThisBuild / organization := "io.github.rustfields"
ThisBuild / scalacOptions ++= Seq("-feature", "-deprecation")

lazy val core = ProjectRef(uri("https://github.com/RustFields/ScaFi-core.git"), "root")

lazy val root = (project in file("."))
  .settings(
    name := "ScaFi-fields",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % "test",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.10.0"
  ).dependsOn(core)
