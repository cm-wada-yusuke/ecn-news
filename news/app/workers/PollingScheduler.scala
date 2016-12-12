package workers

import akka.actor.{ Actor, ActorRef, Cancellable }
import workers.config.NewsConfig

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._

class PollingScheduler(
    receiver: ActorRef,
    config: NewsConfig
) extends Actor {
  override def receive: Receive = PartialFunction.empty

  /**
   * 1秒ごとにpolling
   */
  val polling: Cancellable = context.system.scheduler.schedule(
    0.seconds,
    config.pollingInterval,
    receiver,
    MessageReceiver.Receive
  )

  @scala.throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    if (!polling.isCancelled) {
      polling.cancel()
    }
    super.preRestart(reason, message)
  }
}
