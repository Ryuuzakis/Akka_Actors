package fr.akka_actors.actor

import akka.actor.Actor
import fr.akka_actors.messages.TextMessage
import scala.collection.immutable.List
import akka.actor.ActorRef
import fr.akka_actors.messages.AddNeighbours
import akka.actor.ActorSelection

class Node() extends Actor {
	
	var neighbours : Seq[ActorRef] = Nil
	var name : String = ""
	var visited : Boolean = false
	
	def receive = {
		case AddNeighbours(newNeighbours) =>
			neighbours = neighbours ++ newNeighbours
		
		case TextMessage(res) =>
		  val newValue = res + 1
		  
			if (!visited) {				
				println("Node " + self.path.name + " valeur : " + res)
				neighbours.map { n =>
				  n ! TextMessage(newValue)
				}
				visited = true
			}	
	}
}