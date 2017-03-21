import akka.actor.ActorSystem
import akka.actor.Props
import fr.akka_actors.actor.Master
import fr.akka_actors.actor.Listener
import fr.akka_actors.messages.Work




object Main extends App {
  
  calculate()
  
  def calculate() = {
    val system = ActorSystem("TestSystem")
 
    // create the result listener, which will print the result and shutdown the system
    val listener = system.actorOf(Props[Listener], name = "listener")
 
    // create the master
    val master = system.actorOf(Props(new Master(listener)),
      name = "master")
 
    // start the calculation
    master ! Work(30, 12)
  }
}