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

object Main extends App {

  calculate()

  def calculate() = {
    setupSys1()
    setupSys2()
    val local = ActorSystem("LocalConsole", loadConf("local.conf"))
    val system1Path = "akka.tcp://RemoteSystem1@localhost:5150/user/"
    val system2Path = "akka.tcp://RemoteSystem2@localhost:5151/user/"
    
    //Thread.sleep(4000);
    
    local.actorSelection(system1Path + "node0") ! AddNeighbours(List(system1Path + "node1", system2Path + "node4"))
    local.actorSelection(system1Path + "node1") ! AddNeighbours(List(system1Path + "node0", system1Path + "node2", system2Path + "node3"))
    local.actorSelection(system1Path + "node2") ! AddNeighbours(List(system1Path + "node1"))
    local.actorSelection(system2Path + "node3") ! AddNeighbours(List(system1Path + "node1", system2Path + "node5"))
    local.actorSelection(system2Path + "node4") ! AddNeighbours(List(system1Path + "node0", system2Path + "node5"))
    local.actorSelection(system2Path + "node5") ! AddNeighbours(List(system2Path + "node3", system2Path + "node4"))

    // start the calculation
    local.actorSelection(system1Path + "node0") ! TextMessage(0)
  }

  def loadConf(conf: String) = {
    val configFile = getClass.getClassLoader.getResource(conf).getFile
    ConfigFactory.parseFile(new File(configFile))
  }

  def setupSys1() = {
    val system1 = ActorSystem("RemoteSystem1", loadConf("system1.conf"))
    val system1Addr = AddressFromURIString("akka.tcp://RemoteSystem1@localhost:5150")
    for (i <- 0 to 2) {
      println("actor " + i + " on system1")
      system1.actorOf(Props(new Node(i)).withDeploy(Deploy(scope = RemoteScope(system1Addr))), name = "node" + i)
    }
  }

  def setupSys2() = {
    val system2 = ActorSystem("RemoteSystem2", loadConf("system2.conf"))
    val system2Addr = AddressFromURIString("akka.tcp://RemoteSystem2@localhost:5151")
    for (i <- 3 to 5) {
      println("actor " + i + " on system2")
      system2.actorOf(Props(new Node(i)).withDeploy(Deploy(scope = RemoteScope(system2Addr))), name = "node" + i)
    }
  }
}