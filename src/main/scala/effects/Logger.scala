package effects

import scalaz.zio.{TaskR, ZIO}

trait Logger {

  val log: Logger.Effect

}

object Logger {

  trait Effect {

    def error(message: String): TaskR[Any, Unit]

    def info(message: String): TaskR[Any, Unit]

  }

}

class ConsoleLogger extends Logger.Effect {

  def error(message: String): TaskR[Any, Unit] =
    ZIO.effectTotal(println(s"ERROR: $message"))

  def info(message: String): TaskR[Any, Unit] =
    ZIO.effectTotal(println(s"INFO: $message"))
}
