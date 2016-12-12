package services

import com.google.inject.Inject
import domain.common.ReceivedMessage
import domain.news.NewsMessage

import scala.concurrent.Future

class NewsMessageReceiveService @Inject()(
    queue: NewsQueue
) {

  def receive: Future[List[ReceivedMessage[NewsMessage]]] = queue.receive

}
