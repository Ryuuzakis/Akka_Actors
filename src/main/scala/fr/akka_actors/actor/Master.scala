package fr.akka_actors.actor

import akka.actor.Actor
import fr.akka_actors.messages.Work
import fr.akka_actors.messages.Result
import akka.actor.ActorRef

class Master(listener: ActorRef) extends Actor {
  
  def receive = {
    case Work(a, b) ⇒
      
      listener ! Result(a + b)
      context.stop(self)
  }
}