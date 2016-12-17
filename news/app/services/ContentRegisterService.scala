package services

import javax.inject.Inject

import domain.news.content.Content

import scala.concurrent.Future

class ContentRegisterService @Inject()(
    contentStore: ContentStore
) {

  def register(content: Content): Future[Unit] = contentStore.store(content)

}
