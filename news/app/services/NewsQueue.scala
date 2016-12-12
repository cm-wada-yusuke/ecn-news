package services

import domain.common.{ MessageToken, ReceivedMessage }
import domain.news.NewsMessage

import scala.concurrent.Future

trait NewsQueue {

  def receive: Future[List[ReceivedMessage[NewsMessage]]]

  def delete(token: MessageToken): Future[Unit]

}
