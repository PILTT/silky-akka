package examples.greetings

object Signals {

  sealed trait Signal

  case object Start extends Signal
  case object Stop  extends Signal
}
