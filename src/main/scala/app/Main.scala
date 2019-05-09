package app

import config.ConfigLoader
import scalaz.zio.console.putStrLn
import scalaz.zio.{App, ZIO}

object Main extends App {

  def run(args: List[String]): ZIO[Main.Environment, Nothing, Int] = {

    val program = for {
      _ <- ZIO.fromEither(ConfigLoader.load)
    } yield ()

    program.foldM(e => putStrLn(e.getMessage) *> ZIO.succeed(1),
                  _ => ZIO.succeed(0))

  }
}
