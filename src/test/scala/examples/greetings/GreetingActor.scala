package examples.greetings

import akka.actor.Actor
import silky.akka.ActorAuditing

class GreetingActor extends Actor with ActorAuditing {
  def receive = {
    case Hello(subject)   ⇒ println(s"Hello $subject")
    case Goodbye(subject) ⇒ println(s"Goodbye $subject")
  }
}
