package effects.consumer

import scalaz.zio.TaskR

trait UserMessageConsumer {

  val userMessageConsumer: UserMessageConsumer.Effect

}

object UserMessageConsumer {

  trait Effect {

    def consume(message: String): TaskR[Any, Unit]

  }

}
