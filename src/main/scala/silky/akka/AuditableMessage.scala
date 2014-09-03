package silky.akka

import java.util.UUID

import scala.annotation.implicitNotFound
import scala.xml.NodeSeq

@implicitNotFound("No member of type class AuditableMessage in scope for ${T}")
trait AuditableMessage[T] {
  def messageFlowId(message: T): String
}

object AuditableMessage {
  // TODO figure out how to dispatch to the concrete type
  implicit object AuditableAny extends AuditableMessage[Any] {
    def messageFlowId(message: Any) = UUID.randomUUID().toString
  }

  implicit object AuditableString extends AuditableMessage[String] {
    def messageFlowId(message: String) = UUID.randomUUID().toString
  }

  implicit object AuditableNodeSeq extends AuditableMessage[NodeSeq] {
    def messageFlowId(message: NodeSeq) = UUID.randomUUID().toString
  }
}
