package examples.greetings

import java.util.UUID

import akka.actor.{Actor, ActorRef}
import examples.greetings.Greetings.{Goodbye, Hello}
import examples.greetings.Signals.{Start, Stop}

class MainActor(greetingActor: ActorRef) extends Actor {
   def receive = {
     case Start ⇒ greetingActor ! Hello(subject = s"Silky [ conversationId: ${UUID.randomUUID()} ]")
     case Stop  ⇒ greetingActor ! Goodbye
     case _     ⇒
   }
 }
