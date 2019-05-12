package app

import config.ConfigLoader
import domain.MessageSent
import effects.consumer.SQSClient
import effects.repository.{
  DoobieTransactor,
  MessageRepository,
  MessageRepositoryDoobie
}
import effects.{ConsoleLogger, Logger}
import scalaz.zio.clock.Clock
import scalaz.zio.console.putStrLn
import scalaz.zio.scheduler.Scheduler
import scalaz.zio.{App, ZIO}
import service.MessageService
import zio.sqs.SqsStream

object Main extends App {

  type AppEnvironment = Clock with Logger with MessageRepository

//  type AppTask[A] = TaskR[AppEnvironment, A]

  def run(args: List[String]): ZIO[Main.Environment, Nothing, Int] = {

    val program = for {
      cfg <- ZIO.fromEither(ConfigLoader.load)

      consumer <- ZIO
        .environment[AppEnvironment]
        .flatMap { _ =>
          SqsStream(SQSClient.instantiate(cfg.aws.sqs),
                    cfg.queues.userMessageEvent).foreach { msg =>
            import io.circe.generic.auto._
            import io.circe.parser._

            val xz =
              parse(msg.body()).right.get.hcursor.get[String]("Message")

            val xzz = xz.right.get

            MessageService.appendMessageSentEvent(
              decode[MessageSent](xzz).right.get)

          }
        }
        .provideSome[Environment] { base =>
          new Clock with Logger with MessageRepository {

            val clock: Clock.Service[Any] = base.clock

            val scheduler: Scheduler.Service[Any] = base.scheduler

            val log: Logger.Effect = new ConsoleLogger()

            val messageRepository: MessageRepository.Effect =
              new MessageRepositoryDoobie(
                DoobieTransactor.instantiate(cfg.database))

          }

        }

    } yield consumer

    program.foldM(e => putStrLn(e.getMessage) *> ZIO.succeed(1),
                  _ => ZIO.succeed(0))

  }
}
