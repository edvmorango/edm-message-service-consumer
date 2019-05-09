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

//class UserMessageConsumerSNS(sqsClient: SqsAsyncClient)
//    extends UserMessageConsumer.Effect {
//
//  def error(message: String): TaskR[Any, Unit] =
//    ZIO.effectTotal(println(s"ERROR: $message"))
//
//  def info(message: String): TaskR[Any, Unit] =
//    ZIO.effectTotal(println(s"INFO: $message"))
//}
