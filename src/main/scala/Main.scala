import akka.actor.ActorSystem
import akka.actor.Props
import fr.akka_actors.actor.Node
import fr.akka_actors.messages.TextMessage
import scala.collection.mutable.Node
import akka.actor.ActorRef
import fr.akka_actors.messages.AddNeighbours
import java.io.File
import com.typesafe.config.ConfigFactory




object Main extends App {
  
  calculate()
  
  
  def calculate() = {
    val system = ActorSystem("RemoteSystem1", loadConf("system1.conf"))
 
    val system2 = ActorSystem("RemoteSystem2", loadConf("system2.conf"))
    
    val nodes: Array[ActorRef] = new Array(6)
    
    for(i <- 0 to 5) {
	    nodes(i) = {
	   		if(i <= 2) {	   	    
  	    	system
  	    } else {
  	   	  system2
  	    }
	   	}.actorOf(Props(new Node(i)), name="node" + i)
    }
    
    val system1Path = "akka.tcp://RemoteSystem1@localhost:5150/user/"
    val system2Path = "akka.tcp://RemoteSystem2@localhost:5151/user/"
    
    system.actorSelection(system1Path + "node0") ! AddNeighbours(List(nodes(1), nodes(4)))
    //nodes(0) ! AddNeighbours(List(nodes(1), nodes(4)))
    system.actorSelection(system1Path + "node1") ! AddNeighbours(List(nodes(2), nodes(3)))
    system2.actorSelection(system2Path + "node5") ! AddNeighbours(List(nodes(3), nodes(4)))
    
    // start the calculation
    system.actorSelection("akka.tcp://RemoteSystem1@localhost:5150/user/node0") ! TextMessage(0)
  }
  
  def loadConf(conf: String) = {
    val configFile1 = getClass.getClassLoader.getResource(conf).getFile
    ConfigFactory.parseFile(new File(configFile1))
  }
}