package app

import app.ApplicationEnvironment.AppEnvironment
import config.ConfigLoader
import scalaz.zio.ZSchedule
import service.MessageService
import event.SQSConsumerImpl
import scalaz.zio.console.putStrLn
import scalaz.zio.{App, ZIO}

object Main extends App {

  def run(args: List[String]): ZIO[Main.Environment, Nothing, Int] = {

    val program = for {
      cfg <- ZIO.fromEither(ConfigLoader.load)
      consumer <- ZIO
        .environment[AppEnvironment]
        .flatMap { _ =>
          new SQSConsumerImpl(cfg)
            .messageSentConsumer(10)
            .flatMap(MessageService.appendMessageSentEventsPar)
            .repeat(ZSchedule.forever)
        }
        .provideSome[Environment](ApplicationEnvironment.appEnv(cfg))

    } yield consumer

    program.foldM(e => putStrLn(e.getMessage) *> ZIO.succeed(1),
                  _ => ZIO.succeed(0))

  }
}
