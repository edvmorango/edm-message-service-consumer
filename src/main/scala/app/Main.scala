package app

import config.ConfigLoader
import effects.Logger
import effects.consumer.SQSClient
import scalaz.zio.clock.Clock
import scalaz.zio.console.putStrLn
import scalaz.zio.{App, TaskR, UIO, ZIO}
import zio.sqs.SqsStream

object Main extends App {

  type AppEnvironment = Clock with Logger

  type AppTask[A] = TaskR[AppEnvironment, A]

  def run(args: List[String]): ZIO[Main.Environment, Nothing, Int] = {

    val program = for {
      cfg <- ZIO.fromEither(ConfigLoader.load)

      _ <- SqsStream(SQSClient.instantiate(cfg.aws.sqs),
                     cfg.queues.userMessageEvent).foreach(msg =>
        UIO(println(msg)))

    } yield ()

    program.foldM(e => putStrLn(e.getMessage) *> ZIO.succeed(1),
                  _ => ZIO.succeed(0))

  }
}
