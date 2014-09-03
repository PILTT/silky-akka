package silky.akka

import java.lang.Thread.sleep

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.TestKit
import examples.greetings.Signals.{Start, Stop}
import examples.greetings.{GreetingActor, MainActor}
import org.scalatest.{BeforeAndAfterAll, SpecLike}

class ExampleSpec extends TestKit(ActorSystem("GreetingSystem")) with SpecLike with BeforeAndAfterAll {

  val greetingTranslator = system.actorOf(Props(classOf[GreetingActor]), name = "GreetingTranslator")
  val mainActor = system.actorOf(Props(classOf[MainActor], greetingTranslator), name = "MainActor")

  override protected def afterAll(): Unit = system.shutdown()

  object `Silky Akka` {
    def `captures Akka message flows` {
      sendTo(mainActor, message = Start)
      sendTo(mainActor, message = Stop)

      sendTo(mainActor, message = Start)
      sendTo(mainActor, message = Stop)

      sleep(1000L)
      MessageCollector.logMessages()
    }
  }

  private def sendTo(actor: ActorRef, message: AnyRef): Unit = {
    actor ! message
    sleep(100L)
  }
}
