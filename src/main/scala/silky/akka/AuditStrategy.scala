package silky.akka

import akka.actor.ActorRef

trait AuditStrategy {
  def auditable(from: ActorRef, to: ActorRef, message: Any): Boolean
}

object AuditEverything extends AuditStrategy {
  def auditable(from: ActorRef, to: ActorRef, message: Any): Boolean = true
}

object AuditNothing extends AuditStrategy {
  def auditable(from: ActorRef, to: ActorRef, message: Any): Boolean = false
}

object AuditMessagesFromUserAndRemoteActors extends AuditStrategy {
  def auditable(from: ActorRef, to: ActorRef, message: Any): Boolean = auditable(from) && auditable(to)

  private def auditable(actor: ActorRef): Boolean = actor.path.toString.matches(".+/(user|remote)/.+")
}

object AuditStrategy {
  private[this] var instance: AuditStrategy = AuditNothing

  def is(strategy: AuditStrategy): Unit = instance = strategy
  lazy val auditStrategy: AuditStrategy = instance
}
