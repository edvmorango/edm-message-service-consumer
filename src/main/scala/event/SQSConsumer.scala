package event

import app.ApplicationEnvironment.AppEnvironment
import config.Config
import domain.MessageSent
import effects.consumer.SQSClient
import scalaz.zio.{Chunk, ZIO}
import scalaz.zio.stream.ZSink
import scalaz.zio.stream.ZSink.Step
import software.amazon.awssdk.services.sqs.model.Message
import zio.sqs.SqsStream

trait SQSConsumer {

  def messageSentConsumer(
      limitSize: Int): ZIO[AppEnvironment, Throwable, List[MessageSent]]
}

class SQSConsumerImpl(cfg: Config) extends SQSConsumer {

  private def parseSqsMessage(msg: Message): Either[Throwable, MessageSent] = {
    import io.circe.generic.auto._
    import io.circe.parser._

    for {
      sqsMessage <- parse(msg.body())
      stringBody <- sqsMessage.hcursor.get[String]("Message")
      messageSent <- decode[MessageSent](stringBody)
    } yield messageSent

  }

  def messageSentConsumer(
      limitSize: Int): ZIO[AppEnvironment, Throwable, List[MessageSent]] = {

    val nil = ZIO.succeed(List.empty[MessageSent])

    val collectMessageSent =
      ZSink.foldM[Any, Throwable, Nothing, Message, List[MessageSent]](nil) {
        (acc, a) =>
          parseSqsMessage(a) match {
            case Left(_) =>
              ZIO.apply(Step.more(acc))
            case Right(ms) =>
              if (acc.size == limitSize - 1)
                ZIO.apply(Step.done(ms :: acc, Chunk.empty))
              else
                ZIO.apply(Step.more(ms :: acc))
          }
      }

    SqsStream(SQSClient.instantiate(cfg.aws.sqs), cfg.queues.userMessageEvent)
      .run(collectMessageSent)

  }

}
