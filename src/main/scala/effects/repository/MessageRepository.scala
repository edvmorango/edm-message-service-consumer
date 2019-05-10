package effects.repository

import domain.MessageSent
import doobie.util.transactor.Transactor
import scalaz.zio.{Task, ZIO}

trait MessageRepository {

  val messageRepository: MessageRepository.Effect

}

object MessageRepository {

  trait Effect {

    def insertMessageSent(message: MessageSent): ZIO[Any, Throwable, Unit]

  }

}

class MessageRepositoryDoobie(xa: Transactor[Task])
    extends MessageRepository.Effect {

  import doobie.implicits._
  import io.circe.generic.auto._
  import io.circe.syntax._
  import scalaz.zio.interop.catz._

  def insertMessageSent(message: MessageSent): ZIO[Any, Throwable, Unit] = {

    val query =
      s"""
        |INSERT INTO user_message
        |(uuid, message, sender_uuid, peer_uuid, sender_payload, peer_payload, send_date, created_at, updated_at)
        | values (${message.uuid},
        |         ${message.sender.uuid},
        |         ${message.peer.uuid},
        |         ${message.sender.asJson},
        |         ${message.peer.asJson},
        |         ${message.sendDate},
        |         ${message.sendDate},
        |         ${message.sendDate})
      """.stripMargin

    for {
      _ <- sql"$query".update.run.transact(xa)
    } yield ()

  }
}
