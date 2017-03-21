package fr.akka_actors.actor

import akka.actor.Actor
import fr.akka_actors.messages.TextMessage
import scala.collection.immutable.List
import akka.actor.ActorRef
import fr.akka_actors.messages.AddNeighbours
import fr.akka_actors.messages.SyncNeighbours

class Node(id : Int, var neighbours : Seq[ActorRef] = Nil) extends Actor {
	
	var visited : Boolean = false
	
	def receive = {
		case AddNeighbours(newNeighbours) =>
			neighbours = neighbours ++ newNeighbours
			newNeighbours.map { n =>
				n ! SyncNeighbours(self)
			}
			
		case SyncNeighbours(newNeighbour) =>
			neighbours = neighbours :+ newNeighbour
			
		case TextMessage(res) =>
			if(!visited) {				
				println("Node " + id + " valeur : " + res)
				neighbours.map { n =>
				  n ! TextMessage(res + 1)
				}
				visited = true
			}
				
	}
}