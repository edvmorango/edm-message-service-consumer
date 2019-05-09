package effects.consumer

import java.net.URI

import config.SqsConfig
import software.amazon.awssdk.auth.credentials.{
  AwsBasicCredentials,
  StaticCredentialsProvider
}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient

object SQSClient {

  def instantiate(cfg: SqsConfig): SqsAsyncClient = {

    SqsAsyncClient
      .builder()
      .credentialsProvider(StaticCredentialsProvider.create(
        AwsBasicCredentials.create(cfg.accessKey, cfg.secretKey)))
      .endpointOverride(URI.create(s"http://${cfg.host}:${cfg.port}"))
      .region(Region.of(cfg.region))
      .build()

  }

}
