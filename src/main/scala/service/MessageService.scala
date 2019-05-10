package service

import domain.MessageSent
import effects.repository.MessageRepository
import scalaz.zio.ZIO
import service.Environment.UserMessageServiceEnvironment

trait MessageService[R] {

  def appendMessageSentEvent(message: MessageSent): ZIO[R, Throwable, Unit]

}

object MessageService extends MessageService[UserMessageServiceEnvironment] {
  def appendMessageSentEvent(message: MessageSent)
    : ZIO[UserMessageServiceEnvironment, Throwable, Unit] =
    ZIO.access[MessageRepository] { env =>
      for {
        _ <- info("Preparing to insert MessageSent")
        _ <- env.messageRepository.insertMessageSent(message)
        _ <- info("MessageSet inserted succesfully")
      } yield ()
    }
}
