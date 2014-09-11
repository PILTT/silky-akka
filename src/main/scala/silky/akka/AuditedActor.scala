package silky.akka

import java.util.Date
import java.util.UUID.randomUUID

import akka.actor.{Actor, ActorRef}
import scala.util.Try
import silky.akka.AuditableMessage.messageFlowIdOf
import silky.akka.MessageCollector.messageCollector
import silky.audit.AuditMessage

trait AuditedActor extends Actor {

  override def aroundReceive(receive: Receive, message: Any): Unit = {
    super.aroundReceive(receive, message)

    if (auditingEnabled && auditable(sender()) && auditable(self)) messageCollector += (messageFlowIdOf(message),
      AuditMessage(
        id        = randomUUID().toString,
        from      = nameOf(sender()),
        to        = nameOf(self),
        payload   = message.asInstanceOf[AnyRef],
        timestamp = new Date()
    ))
  }

  private def auditingEnabled: Boolean = Try(context.system.settings.config.getBoolean("akka.actor.auditing-receive")).getOrElse(false)
  private def auditable(actor: ActorRef): Boolean = actor.path.toString.matches(".+/(user|remote)/.+")
  private def nameOf(actor: ActorRef): String = actor.path.name
}
