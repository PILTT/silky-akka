package examples.greetings

import akka.actor.{Actor, ActorRef}
import examples.greetings.Greetings.{Goodbye, Hello}
import examples.greetings.Signals.{Start, Stop}

class MainActor(greetingActor: ActorRef) extends Actor {
   def receive = {
     case Start(conversationId) ⇒ greetingActor !   Hello(subject = s"Silky [ conversationId: $conversationId ]")
     case Stop(conversationId)  ⇒ greetingActor ! Goodbye(subject = s"Silky [ conversationId: $conversationId ]")
   }
 }
