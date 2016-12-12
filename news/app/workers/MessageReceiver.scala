package workers

import akka.actor.{ Actor, ActorLogging, ActorRef }
import akka.event.LoggingReceive
import domain.common.{ ProcessableMessage, UnprocessableMessage }
import services.NewsMessageReceiveService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

class MessageReceiver(
    maintainer: ActorRef,
    deleter: ActorRef,
    receiveService: NewsMessageReceiveService
) extends Actor with ActorLogging {

  override def receive: Receive = LoggingReceive {
    case MessageReceiver.Receive =>
      val future = receiveService.receive

      future.onComplete {
        case Success(messages) =>
          messages.foreach {
            case ProcessableMessage(message, token) =>
              maintainer ! MessageMaintainer.Maintain(message, token)
            case UnprocessableMessage(e, token) =>
              log.warning("Unprocessable messages.", e)
              deleter ! MessageDeleter.Delete(token)
          }
        case Failure(e) =>
          log.warning("Failed to receive news message. ", e)
      }
  }
}

object MessageReceiver {

  case object Receive

}
