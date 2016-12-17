package tasks

import akka.actor.{ Actor, ActorLogging, ActorRef }
import akka.event.LoggingReceive
import domain.news.{ MasterOperation, NewsMessage }
import workers.MessageMaintainer
import workers.MessageMaintainer.Maintain

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

class MasterRegisterTask(
    maintainer: ActorRef
) extends Actor with ActorLogging {


  override def receive: Receive = LoggingReceive {
    case Maintain(message, token) =>
      doTask(message).onComplete {
        case Success(_) => maintainer ! MessageMaintainer.MaintainSucceeded(MasterOperation, token)
        case Failure(e) => maintainer ! MessageMaintainer.MaintainFailed(MasterOperation, token, e)
      }

  }

  private def doTask(newsMessage: NewsMessage): Future[Unit] = {
    // TODO :最終的にはRDSに書き込むが、ひとまずはログに書き出すところまで。
    Future { log.info("Write RDS user content data.") }
  }
}
