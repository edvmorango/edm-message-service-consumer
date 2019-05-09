package app

import config.ConfigLoader
import effects.Logger
import scalaz.zio.clock.Clock
import scalaz.zio.console.putStrLn
import scalaz.zio.{App, TaskR, ZIO}

object Main extends App {

  type AppEnvironment = Clock with Logger

  type AppTask[A] = TaskR[AppEnvironment, A]

  def run(args: List[String]): ZIO[Main.Environment, Nothing, Int] = {

    val program = for {
      _ <- ZIO.fromEither(ConfigLoader.load)
    } yield ()

    program.foldM(e => putStrLn(e.getMessage) *> ZIO.succeed(1),
                  _ => ZIO.succeed(0))

  }
}
