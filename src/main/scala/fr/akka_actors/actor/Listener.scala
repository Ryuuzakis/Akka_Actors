package fr.akka_actors.actor

import akka.actor.Actor
import fr.akka_actors.messages.Result

class Listener extends Actor {
  
  
    def receive = {
      case Result(res) ⇒
        println("resultat : " + res)
        context.system.shutdown()
    }
}