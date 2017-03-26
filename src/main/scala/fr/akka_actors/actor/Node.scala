package fr.akka_actors.actor

import akka.actor.Actor
import fr.akka_actors.messages.TextMessage
import scala.collection.immutable.List
import akka.actor.ActorRef
import fr.akka_actors.messages.AddNeighbours
import akka.actor.ActorSelection

class Node(id : Int, var neighbours : Seq[String] = Nil) extends Actor {
	
	var visited : Boolean = false
	
	def receive = {
		case AddNeighbours(newNeighbours) =>
			neighbours = neighbours ++ newNeighbours

		case TextMessage(res) =>
			if(!visited) {				
				println("Node " + id + " valeur : " + res)
				neighbours.map { n =>
				  context.actorSelection(n) ! TextMessage(res + 1)
				}
				visited = true
			}
				
	}
}