package workers

import javax.inject.Inject

import akka.actor.{ Actor, ActorRef, Props }
import services.{ NewsMessageDeleteService, NewsMessageReceiveService }
import workers.NewsProcessSupervisor.{ Ping, Pong }
import workers.config.NewsConfig

class NewsProcessSupervisor @Inject()(
    receiveService: NewsMessageReceiveService,
    deleteService: NewsMessageDeleteService,
    config: NewsConfig
) extends Actor {

  /**
   * workers
   */
  val deleter: ActorRef = context.actorOf(Props(
    classOf[MessageDeleter], deleteService
  ), "MessageDeleter")

  val maintainer: ActorRef = context.actorOf(Props(
    classOf[MessageMaintainer], deleter
  ), "MessageMaintainer")

  val receiver: ActorRef = context.actorOf(Props(
    classOf[MessageReceiver], maintainer, deleter, receiveService
  ), "MessageReceiver")

  val pollingScheduler: ActorRef = context.actorOf(Props(
    classOf[PollingScheduler], receiver, config
  ), "PollingScheduler")


  override def receive: Receive = {
    case Ping => sender ! Pong
  }
}

object NewsProcessSupervisor {

  case object Ping

  case object Pong

}
