package tasks

import akka.actor.{ Actor, ActorLogging, ActorRef }
import akka.event.LoggingReceive
import domain.news.content.{ Content, ContentJob }
import domain.news.{ ContentOperation, NewsMessage }
import services.ContentRegisterService
import workers.MessageMaintainer
import workers.MessageMaintainer.Maintain

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

class ContentRegisterTask(
    maintainer: ActorRef,
    contentRegisterService: ContentRegisterService
) extends Actor with ActorLogging {

  override def receive: Receive = LoggingReceive {
    case Maintain(message, token) =>
      doTask(message).onComplete {
        case Success(_) => maintainer ! MessageMaintainer.MaintainSucceeded(ContentOperation, token)
        case Failure(e) => maintainer ! MessageMaintainer.MaintainFailed(ContentOperation, token, e)
      }
  }

  private def doTask(newsMessage: NewsMessage): Future[Unit] =
    contentRegisterService.register(convertToContent(newsMessage)).map { _ =>
      log.info("Write DynamoDB user content data.")
    }


  private def convertToContent(newsMessage: NewsMessage): Content = {
    val job = newsMessage.job.asInstanceOf[ContentJob]
    Content(
      userId = job.userId,
      newsId = job.newsId
    )
  }
}
