import effects.Logger
import scalaz.zio.ZIO

package object service {

  def info(message: String): ZIO[Logger, Throwable, Unit] =
    ZIO.accessM[Logger](_.log.info(message))

  def error(message: String): ZIO[Logger, Throwable, Unit] =
    ZIO.accessM[Logger](_.log.info(message))

}
