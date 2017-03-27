import akka.actor.ActorSystem
import akka.actor.Props
import fr.akka_actors.actor.Node
import fr.akka_actors.messages.TextMessage
import scala.collection.mutable.Node
import akka.actor.ActorRef
import fr.akka_actors.messages.AddNeighbours
import java.io.File
import com.typesafe.config.ConfigFactory
import akka.actor.Deploy
import akka.actor.AddressFromURIString
import akka.remote.RemoteScope
import akka.actor.Address

object Main extends App {

  calculate()

  def calculate() = {
    if(args(0).equals("sys1")) {
      setupSys1()
    } else if(args(0).equals("sys2")) {
      setupSys2()
    } else if(args(0).equals("console")) {
      console()
    }
  }

  def loadConf(conf: String) = {
    val configFile = getClass.getClassLoader.getResource(conf).getFile
    ConfigFactory.parseFile(new File(configFile))
  }
  
  
  def readUserCommands() {
	  scala.io.StdIn.readLine()
  }
  
  def console() = {
    val local = ActorSystem("LocalConsole", loadConf("local.conf"))
    val system1Path = "akka.tcp://RemoteSystem1@localhost:5150/user/"
    val system2Path = "akka.tcp://RemoteSystem2@localhost:5151/user/"
    
    val addrSys1 = AddressFromURIString("akka.tcp://RemoteSystem1@localhost:5150");
    val addrSys2 = AddressFromURIString("akka.tcp://RemoteSystem2@localhost:5151");
    
    var nodes : Array[ActorRef] = Array()
    
    nodes = nodes :+ newRemoteNode(local, addrSys1, "node0")
    nodes = nodes :+ newRemoteNode(local, addrSys1, "node1")
    nodes = nodes :+ newRemoteNode(local, addrSys1, "node2")
    nodes = nodes :+ newRemoteNode(local, addrSys2, "node3")
    nodes = nodes :+ newRemoteNode(local, addrSys2, "node4")
    nodes = nodes :+ newRemoteNode(local, addrSys2, "node5")
    
    
    newLink(local, nodes(0), nodes(1))
    newLink(local, nodes(0), nodes(4))
    newLink(local, nodes(1), nodes(2))
    newLink(local, nodes(1), nodes(3))
    newLink(local, nodes(5), nodes(3))
    newLink(local, nodes(4), nodes(5))
    /*
    local.actorSelection(system1Path + "node0") ! AddNeighbours(List(system1Path + "node1", system2Path + "node4"))
    local.actorSelection(system1Path + "node1") ! AddNeighbours(List(system1Path + "node0", system1Path + "node2", system2Path + "node3"))
    local.actorSelection(system1Path + "node2") ! AddNeighbours(List(system1Path + "node1"))
    local.actorSelection(system2Path + "node3") ! AddNeighbours(List(system1Path + "node1", system2Path + "node5"))
    local.actorSelection(system2Path + "node4") ! AddNeighbours(List(system1Path + "node0", system2Path + "node5"))
    local.actorSelection(system2Path + "node5") ! AddNeighbours(List(system2Path + "node3", system2Path + "node4"))

    // start the calculation
    local.actorSelection(system1Path + "node0") ! TextMessage(0)*/
    
    nodes(0) ! TextMessage(0)
  }
  
  def newRemoteNode(local: ActorSystem, addr : Address, nodeName: String) : ActorRef = {
	  local.actorOf(Props[Node].withDeploy(Deploy(scope = RemoteScope(addr))), name = nodeName)
  }
  
  def newLink(local: ActorSystem, node1: ActorRef, node2: ActorRef) = {
	  node1 ! AddNeighbours(List(node2))
	  node2 ! AddNeighbours(List(node1))
  }

  
  def setupSys1() = {
    val system1 = ActorSystem("RemoteSystem1", loadConf("system1.conf"))
  }

  def setupSys2() = {
    val system2 = ActorSystem("RemoteSystem2", loadConf("system2.conf"))
  }
}