package examples.greetings

object Greetings {

  sealed trait Greeting

  case class Hello(subject: String)   extends Greeting
  case class Goodbye(subject: String) extends Greeting
}
