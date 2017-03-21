import akka.actor.ActorSystem
import akka.actor.Props
import fr.akka_actors.actor.Node
import fr.akka_actors.messages.TextMessage
import scala.collection.mutable.Node
import akka.actor.ActorRef
import fr.akka_actors.messages.AddNeighbours




object Main extends App {
  
  calculate()
  
  def calculate() = {
    val system = ActorSystem("TestSystem")
    val system2 = ActorSystem("TestSystem2")
    
    val nodes: Array[ActorRef] = new Array(6)
    
    for(i <- 0 to 5) {
	    nodes(i) = {
	   		if(i % 2 == 0) {	   	    
  	    	system
  	    } else {
  	   	  system2
  	    }
	   	}.actorOf(Props(new Node(i + 1)), name="node" + i+1)
    }
    
    system.actorSelection("node1") ! AddNeighbours(List(nodes(1), nodes(4)))
    //nodes(0) ! AddNeighbours(List(nodes(1), nodes(4)))
    nodes(1) ! AddNeighbours(List(nodes(2), nodes(3)))
    nodes(5) ! AddNeighbours(List(nodes(3), nodes(4)))
    
    // start the calculation
    nodes(0) ! TextMessage(0)
  }
}