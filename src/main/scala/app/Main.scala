package app

import app.ApplicationEnvironment.AppEnvironment
import config.ConfigLoader
import scalaz.zio.ZSchedule
//import domain.MessageSent
//import effects.consumer.SQSClient
import event.SQSConsumerImpl
import scalaz.zio.console.putStrLn
import scalaz.zio.{App, ZIO}
//import service.MessageService
//import zio.sqs.SqsStream

object Main extends App {

  def run(args: List[String]): ZIO[Main.Environment, Nothing, Int] = {

    val program = for {
      cfg <- ZIO.fromEither(ConfigLoader.load)
      consumer <- ZIO
        .environment[AppEnvironment]
        .flatMap { _ =>
          new SQSConsumerImpl(cfg)
            .messageSentConsumer(2)
            .flatMap(m => ZIO(m.foreach(println(_))))
            .repeat(ZSchedule.forever)
        }
        .provideSome[Environment](ApplicationEnvironment.appEnv(cfg))

    } yield consumer

    program.foldM(e => putStrLn(e.getMessage) *> ZIO.succeed(1),
                  _ => ZIO.succeed(0))

  }
}
