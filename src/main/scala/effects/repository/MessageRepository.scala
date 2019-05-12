package effects.repository

import java.sql.Timestamp
import java.util.UUID

import cats.data.NonEmptyList
import domain.MessageSent
import doobie.util.transactor.Transactor
import org.postgresql.util.PGobject
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

  import doobie._
  import doobie.implicits._
  import io.circe.Json
  import scalaz.zio.interop.catz._
  import io.circe.generic.auto._
  import io.circe.syntax._
  import doobie.postgres.implicits._

  implicit val jsonPut: Put[Json] =
    Put.Advanced.other[PGobject](NonEmptyList.of("json")).tcontramap[Json] {
      j =>
        val o = new PGobject
        o.setType("json")
        o.setValue(j.noSpaces)
        o
    }

  def insertMessageSent(message: MessageSent): ZIO[Any, Throwable, Unit] = {

    def query =
      sql"""
        |INSERT INTO user_message
        |(uuid, message, sender_uuid, peer_uuid, sender_payload, peer_payload, send_date, created_at, updated_at)
        | values (${UUID.fromString(message.uuid)},
        |         ${message.message},
        |         ${UUID.fromString(message.sender.uuid)},
        |         ${UUID.fromString(message.peer.uuid)},
        |         ${message.sender.asJson},
        |         ${message.peer.asJson},
        |         ${Timestamp.valueOf(message.sendDate)},
        |         ${Timestamp.valueOf(message.sendDate)},
        |         ${Timestamp.valueOf(message.sendDate)})
      """.stripMargin

    for {
      _ <- query.update.run.transact(xa)
    } yield ()

  }
}
