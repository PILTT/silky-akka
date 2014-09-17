package silky.akka

import java.util.Date
import java.util.UUID.randomUUID

import akka.actor.{Actor, ActorRef}
import scala.util.Try
import silky.akka.AuditableMessage.messageFlowIdOf
import silky.akka.AuditStrategy.auditStrategy
import silky.akka.MessageCollector.messageCollector
import silky.audit.AuditMessage

trait ActorAuditing extends Actor {

  override def aroundReceive(receive: Receive, message: Any): Unit = {
    if (auditingEnabled && auditStrategy.auditable(from = sender(), to = self, message))
      messageCollector += (messageFlowIdOf(message),
        AuditMessage(
          id        = randomUUID().toString,
          from      = nameOf(sender()),
          to        = nameOf(self),
          payload   = message.asInstanceOf[AnyRef],
          timestamp = new Date()
        ))

    super.aroundReceive(receive, message)
  }

  private def auditingEnabled: Boolean = Try(context.system.settings.config.getBoolean("akka.actor.auditing-receive")).getOrElse(false)
  private def nameOf(actor: ActorRef): String = actor.path.name
}
