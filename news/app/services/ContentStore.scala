package services

import domain.news.content.Content

import scala.concurrent.Future

trait ContentStore {

  def store(content: Content): Future[Unit]

}
