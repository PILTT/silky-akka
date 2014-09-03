package silky.akka

import java.util.Date

import akka.actor.{Actor, ActorRef}
import silky.audit.AuditMessage

trait AuditedActor extends Actor {

  override def aroundReceive(receive: Receive, message: Any): Unit = {
    super.aroundReceive(receive, message)
    MessageCollector += AuditMessage(
      id        = messageFlowId(message),
      from      = nameOf(sender()),
      to        = nameOf(self),
      payload   = message.asInstanceOf[AnyRef],
      timestamp = new Date()
    )
  }

  private def messageFlowId[T](message: T)(implicit ev: AuditableMessage[T]): String = ev.messageFlowId(message)
  private def auditable(actor: ActorRef): Boolean = actor.path.toString.matches(".+/(user|remote)/.+")
  private def nameOf(actor: ActorRef): String = actor.path.name
}
