package fr.akka_actors.messages

import akka.actor.ActorRef

sealed trait Message

case class TextMessage(res: Int) extends Message
case class AddNeighbours(neighbours: List[ActorRef]) extends Message
case class SyncNeighbours(neighbour: ActorRef) extends Message