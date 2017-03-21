package fr.akka_actors.messages

sealed trait Message

case class Work(a: Int, b: Int)
case class Result(res: Int)