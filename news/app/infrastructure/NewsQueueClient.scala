package infrastructure

import javax.inject.{ Inject, Named }

import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import domain.common.{ MessageToken, ReceivedMessage }
import domain.news.NewsMessage
import infrastructure.config.NewsQueueConfig
import infrastructures.common.ReceivedMessageConverters
import play.api.Logger
import services.NewsQueue

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success, Try }

class NewsQueueClient @Inject()(
    sqsClient: AmazonSQSClient,
    config: NewsQueueConfig
)(implicit @Named("aws") ec: ExecutionContext) extends NewsQueue {

  override def receive: Future[List[ReceivedMessage[NewsMessage]]] = Future {
    import NewsMessageConverters.NewsMessageFormat

    import scala.collection.JavaConverters._

    Try {
      val req = new ReceiveMessageRequest(config.url)
      sqsClient.receiveMessage(req)
    } match {
      case Success(r) =>
        r.getMessages.asScala.toList map { message =>
          ReceivedMessageConverters.toReceivedMessage(message)(NewsMessageFormat)
        }
      case Failure(e) =>
        Logger.error("ReceiveMessage Failed.", e)
        throw new IllegalArgumentException(e)
    }


  }

  override def delete(token: MessageToken): Future[Unit] = Future {
    sqsClient.deleteMessage(config.url, token.identifier)
  }
}
