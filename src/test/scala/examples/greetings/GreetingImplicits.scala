package examples.greetings

import examples.greetings.Greetings._
import silky.akka.AuditableMessage

object GreetingImplicits {

  implicit object AuditableHello extends AuditableMessage[Hello] {
    def messageFlowId(message: Hello) = conversationIdOf(message.subject)
  }

  implicit object AuditableGoodbye extends AuditableMessage[Goodbye] {
    def messageFlowId(message: Goodbye) = conversationIdOf(message.subject)
  }

  private def conversationIdOf(subject: String): String = """(?<=[ conversationId: ).+(?= ])""".r.findAllIn(subject).group(1)
}
