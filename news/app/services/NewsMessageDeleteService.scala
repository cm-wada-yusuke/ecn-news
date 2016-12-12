package services

import javax.inject.Inject

import domain.common.MessageToken

import scala.concurrent.Future

class NewsMessageDeleteService @Inject()(
    queue: NewsQueue
) {

  def delete(messageToken: MessageToken): Future[Unit] = queue.delete(messageToken)

}
