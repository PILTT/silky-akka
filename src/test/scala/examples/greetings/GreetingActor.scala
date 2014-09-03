package examples.greetings

import examples.greetings.Greetings.{Goodbye, Hello}
import silky.akka.AuditedActor

class GreetingActor extends AuditedActor {
  def receive = {
    case Hello(subject)   ⇒ println(s"Hello $subject")
    case Goodbye(subject) ⇒ println(s"Goodbye $subject")
  }
}
