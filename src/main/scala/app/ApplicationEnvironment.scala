package app

import config.Config
import effects.repository.{
  DoobieTransactor,
  MessageRepository,
  MessageRepositoryDoobie
}
import effects.{ConsoleLogger, Logger}
import scalaz.zio.TaskR
import scalaz.zio.blocking.Blocking
import scalaz.zio.clock.Clock
import scalaz.zio.console.Console
import scalaz.zio.random.Random
import scalaz.zio.scheduler.Scheduler
import scalaz.zio.system.System

object ApplicationEnvironment {

  type AppEnvironment = Clock with Logger with MessageRepository

  type AppTask[A] = TaskR[AppEnvironment, A]

  def appEnv(cfg: Config)(
      base: Clock with Console with System with Random with Blocking) =
    new Clock with Logger with MessageRepository {

      val clock: Clock.Service[Any] = base.clock

      val scheduler: Scheduler.Service[Any] = base.scheduler

      val log: Logger.Effect = new ConsoleLogger()

      val messageRepository: MessageRepository.Effect =
        new MessageRepositoryDoobie(DoobieTransactor.instantiate(cfg.database))

    }

}
