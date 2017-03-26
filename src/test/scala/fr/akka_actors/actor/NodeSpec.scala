package fr.akka_actors.actor

import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import akka.actor.ActorSystem
import akka.actor.Props

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.testkit.{ TestActors, TestKit, ImplicitSender }
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll
import fr.akka_actors.messages.TextMessage
import akka.testkit.TestProbe
import fr.akka_actors.messages.AddNeighbours

class NodeSpec extends TestKit(ActorSystem("NodeSpec")) with ImplicitSender with Matchers with WordSpecLike  {
  
  "a node" must {
    "send a received message to its neighbours with incremented id" in {
      val probe = TestProbe()
      val id = 42
      val messageSent = TextMessage(id)
      val expected = TextMessage(id + 1)
      
      val node = system.actorOf(Props(new Node(1)), name="node1")
      
      node ! AddNeighbours(List(probe.ref.path.toString))
      node ! messageSent
      
      probe.expectMsg(expected)
    }
  }
}