
name := """Akka_actors"""

mainClass in (Compile, run) := Some("Main")
version := "1.0-SNAPSHOT"

scalaVersion := "2.11.5"

lazy val root = (project in file("."))

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.4.0"
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.9"
libraryDependencies += "com.typesafe.akka" % "akka-remote_2.11" % "2.3.9"
libraryDependencies += "com.typesafe" % "config" % "1.2.1"
libraryDependencies += "org.scala-lang" % "scala-library" % "2.11.5"
libraryDependencies += "io.netty" % "netty" % "3.8.0.Final"
libraryDependencies += "com.google.protobuf" % "protobuf-java" % "2.5.0"
libraryDependencies += "org.uncommons.maths" % "uncommons-maths" % "1.2.2a"
