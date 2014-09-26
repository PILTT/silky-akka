package silky.akka

import java.util.UUID.randomUUID

import silky.MessageFlowId

import scala.annotation.implicitNotFound
import scala.language.existentials
import scala.reflect.{ClassTag, classTag}

@implicitNotFound("No member of type class AuditableMessage in scope for ${T}")
trait AuditableMessage[T] {
  def classify:      T ⇒ Option[String]
  def messageFlowId: T ⇒ Option[MessageFlowId]
  def format:        T ⇒ Option[String] = message ⇒ None
}

object AuditableMessage {
  private[this] var extractors: List[Extractor] = List.empty

  def addExtractorFor[T : ClassTag](instance: AuditableMessage[T]): Unit = extractors :+= Extractor(instance)

  def classificationOf(message: Any, default: String = "Unknown"): String =
    extract(extractor ⇒ extractor.instance.classify(message.asInstanceOf[extractor.Type]), message,
      defaultValue = default)

  def messageFlowIdOf(message: Any): MessageFlowId =
    extract(extractor ⇒ extractor.instance.messageFlowId(message.asInstanceOf[extractor.Type]), message,
      defaultValue = MessageFlowId(randomUUID().toString))

  def format(message: AnyRef): AnyRef =
    extract(extractor ⇒ extractor.instance.format(message.asInstanceOf[extractor.Type]), message, defaultValue = message)

  private def extract[V](f: Extractor ⇒ Option[V], message: Any, defaultValue: V): V =
    extractors.find(_.clazz isAssignableFrom message.getClass).flatMap(f).getOrElse(defaultValue)
}

private sealed trait Extractor {
  type Type
  def clazz: Class[_]
  def instance: AuditableMessage[Type]
}

private object Extractor {
  type Aux[T] = Extractor{type Type = T}
  def apply[T : ClassTag](inst: AuditableMessage[T]) = new Extractor {
    type Type = T
    val clazz = classTag[T].runtimeClass
    val instance = inst
  }
}
