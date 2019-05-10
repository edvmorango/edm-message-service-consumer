package effects.repository

import config.DatabaseConfig
import doobie.util.transactor.Transactor
import scalaz.zio.Task
import scalaz.zio.interop.catz._

object DoobieTransactor {

  def instantiate(cfg: DatabaseConfig): Transactor[Task] = {
    Transactor
      .fromDriverManager[Task](cfg.driver, cfg.url, cfg.user, cfg.password)
  }

}
