package examples.greetings

import examples.greetings.Greetings._
import silky.MessageFlowId
import silky.akka.AuditableMessage
import silky.akka.AuditableMessage.addExtractorFor

object GreetingImplicits {

  def addExtractors(): Unit = {
    addExtractorFor(AuditableHello)
    addExtractorFor(AuditableGoodbye)
  }

  implicit object AuditableHello extends AuditableMessage[Hello] {
    def classify      = (message: Hello) ⇒ classificationOf(message)
    def messageFlowId = (message: Hello) ⇒ messageFlowIdOf(message.subject)
  }

  implicit object AuditableGoodbye extends AuditableMessage[Goodbye] {
    def classify      = (message: Goodbye) ⇒ classificationOf(message)
    def messageFlowId = (message: Goodbye) ⇒ messageFlowIdOf(message.subject)
  }

  private def classificationOf(message: Greeting): Option[String] = Some(message.getClass.getSimpleName)

  private def messageFlowIdOf(subject: String): Option[MessageFlowId] =
    """(?<=\[ conversationId: ).+(?= \])""".r.findFirstIn(subject).map(MessageFlowId(_))
}
