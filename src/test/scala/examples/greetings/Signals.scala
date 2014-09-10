package examples.greetings

object Signals {

  sealed trait Signal { val conversationId: String }

  case class Start(conversationId: String) extends Signal
  case class Stop(conversationId: String)  extends Signal
}
