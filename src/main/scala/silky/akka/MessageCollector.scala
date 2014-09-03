package silky.akka

import silky.MessageFlowId
import silky.audit.{Slf4jMessageAuditor, Formatter, AuditMessage}

import scala.collection.mutable

object MessageCollector {
  private val auditor = new Slf4jMessageAuditor("audit", new Formatter)
  private val auditMessages = new mutable.HashSet[AuditMessage]()

  def +=(message: AuditMessage): Unit = auditMessages += message

  def messages: Set[AuditMessage] = auditMessages.toSeq.sortBy(x ⇒ x.timestamp).toSet

  def logMessages(): Unit = messages.foreach { m ⇒ auditor.audit(MessageFlowId.makeOneUp.get, m) }
}
