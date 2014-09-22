package silky.akka

import java.lang.Thread.sleep

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import clairvoyance.plugins.SequenceDiagram
import clairvoyance.scalatest.ClairvoyantContext
import clairvoyance.scalatest.tags.{skipSpecification, skipInteractions}
import com.typesafe.config.{ConfigFactory, ConfigValueFactory}
import examples.greetings.Signals.{Start, Stop}
import examples.greetings.{GreetingActor, GreetingImplicits, MainActor}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, SpecLike}
import silky.MessageFlowId
import silky.akka.AuditableMessage.{classificationOf, format}
import silky.akka.{SimpleMessageCollector ⇒ collector}

@skipSpecification
@skipInteractions
class ExampleSpec extends TestKit(
  ActorSystem(
    name   = "GreetingSystem",
    config = ConfigFactory.empty().withValue("akka.actor.auditing-receive", ConfigValueFactory.fromAnyRef(true))
  ))
  with SpecLike
  with MustMatchers
  with ClairvoyantContext
  with SequenceDiagram
  with BeforeAndAfterAll {

  GreetingImplicits.addExtractors()
  AuditStrategy is AuditMessagesFromUserAndRemoteActors
  MessageCollector uses SimpleMessageCollector

  val greetingActor = system.actorOf(Props(classOf[GreetingActor]), name = classOf[GreetingActor].getSimpleName)
  val mainActor = system.actorOf(Props(classOf[MainActor], greetingActor), name = classOf[MainActor].getSimpleName)

  override def capturedInputsAndOutputs = Seq(this)

  override protected def afterExecution(testName: String) = {
    collector.messages.foreach(m ⇒ captureValue(s"${classificationOf(m.payload)} from ${m.from} to ${m.to}", format(m.payload)))
    collector.audit()
    collector.clear()
  }

  override protected def afterAll() = system.shutdown()

  object `Silky Akka` {
    val conversationId1 = "conversation #1"
    val conversationId2 = "conversation #2"

    def `captures Akka message flows` {
      mainActor ! Start(conversationId = conversationId1); snooze()
      mainActor ! Start(conversationId = conversationId2); snooze()
      mainActor !  Stop(conversationId = conversationId2); snooze()
      mainActor !  Stop(conversationId = conversationId1); snooze()

      collector.messagesFor(MessageFlowId(conversationId1)) must have size 2
      collector.messagesFor(MessageFlowId(conversationId2)) must have size 2
    }
  }

  private def snooze(): Unit = sleep(100L)
}
