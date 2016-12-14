package workers

import akka.actor.{ Actor, ActorLogging, ActorRef, Props }
import akka.event.LoggingReceive
import domain.common.MessageToken
import domain.news.NewsMessage
import domain.news.{ ContentOperation, MasterOperation, Operation }
import tasks.{ ContentRegisterTask, MasterRegisterTask }
import workers.MessageMaintainer.{ Maintain, MaintainFailed, MaintainSucceeded }

class MessageMaintainer(
    deleter: ActorRef
) extends Actor with ActorLogging {

  /**
   * tasks
   */
  val masterRegisterTask: ActorRef = context.actorOf(Props(
    classOf[MasterRegisterTask], self
  ), "MasterRegisterTask")

  val contentRegisterTask: ActorRef = context.actorOf(Props(
    classOf[ContentRegisterTask], self
  ), "ContentRegisterTask")


  override def receive: Receive = LoggingReceive {
    case m@Maintain(message, messageToken) =>
      log.info(s"News job start:${ message.operation }, $messageToken")
      message.operation match {
        case MasterOperation => masterRegisterTask ! m
        case ContentOperation => contentRegisterTask ! m
      }
    case MaintainSucceeded(operation, messageToken) =>
      log.info(s"News Job succeeded:$operation, $messageToken")
      deleter ! MessageDeleter.Delete(messageToken)
    case MaintainFailed(operation, messageToken, e) =>
      log.warning(s"News Job succeeded:$operation, $messageToken", e)
      deleter ! MessageDeleter.Delete(messageToken)
  }
}

object MessageMaintainer {

  case class Maintain(
      message: NewsMessage,
      messageToken: MessageToken
  )

  case class MaintainSucceeded(
      operation: Operation,
      messageToken: MessageToken
  )

  case class MaintainFailed(
      operation: Operation,
      messageToken: MessageToken,
      e: Throwable
  )

}
