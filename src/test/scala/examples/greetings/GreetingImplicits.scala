package examples.greetings

import silky.{MessageFlowId, TreeString}
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

    override def format = (message: Hello) ⇒ Some(message.asTreeString)
  }

  implicit object AuditableGoodbye extends AuditableMessage[Goodbye] {
    def classify      = (message: Goodbye) ⇒ classificationOf(message)
    def messageFlowId = (message: Goodbye) ⇒ messageFlowIdOf(message.subject)

    override def format = (message: Goodbye) ⇒ Some(message.asTreeString)
  }

  private def classificationOf(message: Greeting): Option[String] = Some(message.getClass.getSimpleName)

  private def messageFlowIdOf(subject: String): Option[MessageFlowId] =
    """(?<=\[ conversationId: ).+(?= \])""".r.findFirstIn(subject).map(MessageFlowId(_))
}
