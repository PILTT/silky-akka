package silky.akka

import silky.MessageFlowId
import silky.audit.{AuditMessage, Formatter, Slf4jMessageAuditor}

import scala.collection.{SortedSet, mutable}

object SimpleMessageCollector extends MessageCollector {
  private[this] val auditor = new Slf4jMessageAuditor("audit", new Formatter)
  private[this] val auditMessages = new mutable.HashMap[MessageFlowId, mutable.Set[AuditMessage]]() with mutable.MultiMap[MessageFlowId, AuditMessage]

  def +=(messageFlowId: MessageFlowId, message: AuditMessage): Unit = auditMessages addBinding (messageFlowId, message)
  def forget(messageFlowId: MessageFlowId): Unit = auditMessages.remove(messageFlowId)
  def clear(): Unit = auditMessages.clear()

  implicit val messageOrdering: Ordering[AuditMessage] = Ordering.by((_: AuditMessage).timestamp)
  def messagesFor(messageFlowId: MessageFlowId): SortedSet[AuditMessage] = auditMessages(messageFlowId).to[SortedSet]
  def messages: SortedSet[AuditMessage] = auditMessages.values.flatten.to[SortedSet]

  def audit(): Unit = auditMessages.keys.foreach { id ⇒ messagesFor(id) foreach { m ⇒ auditor.audit(id, m) } }
}
