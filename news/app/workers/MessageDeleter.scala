package workers

import akka.actor.{ Actor, ActorLogging }
import akka.event.LoggingReceive
import domain.common.MessageToken
import services.NewsMessageDeleteService
import workers.MessageDeleter.Delete

import scala.concurrent.ExecutionContext.Implicits.global

class MessageDeleter(
    deleteService: NewsMessageDeleteService
) extends Actor with ActorLogging {
  override def receive: Receive = LoggingReceive {
    case Delete(token) =>
      val future = deleteService.delete(token)
      future.onFailure {
        case e: Throwable => log.warning("Failed to delete news message.", e)
      }
  }
}

object MessageDeleter {

  case class Delete(token: MessageToken)

}
