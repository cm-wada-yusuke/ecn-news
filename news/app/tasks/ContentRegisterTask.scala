package tasks

import akka.actor.{ Actor, ActorLogging, ActorRef }
import akka.event.LoggingReceive
import domain.news.{ ContentOperation, NewsMessage }
import workers.MessageMaintainer
import workers.MessageMaintainer.Maintain

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

class ContentRegisterTask(
    maintainer: ActorRef
) extends Actor with ActorLogging {

  override def receive: Receive = LoggingReceive {
    case Maintain(message, token) =>
      doTask(message).onComplete {
        case Success(_) => maintainer ! MessageMaintainer.MaintainSucceeded(ContentOperation, token)
        case Failure(e) => maintainer ! MessageMaintainer.MaintainFailed(ContentOperation, token, e)
      }
  }

  private def doTask(newsMessage: NewsMessage): Future[Unit] = {
    // TODO: 最終的にはDynamoDBに書き込むが、ひとまずはログに書き出すところまで。
    Future { log.info("Write DynamoDB user content data.") }
  }
}
