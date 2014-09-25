package examples.greetings

sealed trait Greeting { val subject: String }

case class Hello(subject: String)   extends Greeting
case class Goodbye(subject: String) extends Greeting
