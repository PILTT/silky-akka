package silky.akka

import silky.MessageFlowId
import silky.audit.AuditMessage

trait MessageCollector {
  def +=(messageFlowId: MessageFlowId, message: AuditMessage): Unit
}

object NoOpMessageCollector extends MessageCollector {
  def +=(messageFlowId: MessageFlowId, message: AuditMessage) = ()
}

object MessageCollector {
  private[this] var instance: MessageCollector = NoOpMessageCollector

  def uses(messageCollector: MessageCollector): Unit = instance = messageCollector
  lazy val messageCollector: MessageCollector = instance
}
