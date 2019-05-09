package config

final case class Config(app: AppConfig, aws: AWSConfig, queues: QueuesConfig)

final case class AppConfig(host: String, context: String, port: Int)

final case class AWSConfig(sqs: SqsConfig)

final case class SqsConfig(prefix: String,
                           region: String,
                           host: String,
                           port: Int,
                           accessKey: String,
                           secretKey: String)

final case class QueuesConfig(userMessageEvent: String)

final case class UserServiceConfig(baseUri: String)

object ConfigLoader {
  import java.nio.file.Path
  import pureconfig.generic.auto._
  import scala.util.Try
  import pureconfig.module.yaml._

  def load: Either[Throwable, Config] = {

    val path = Path of ClassLoader.getSystemResource("application.yaml").getPath

    Try(loadYamlOrThrow[Config](path)).toEither

  }

}
