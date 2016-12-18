package infrastructure

import javax.inject.{ Inject, Named }

import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec
import com.amazonaws.services.dynamodbv2.document.utils.NameMap
import com.amazonaws.services.dynamodbv2.document.{ DynamoDB, Item, PrimaryKey }
import domain.news.content.Content
import infrastructure.ContentDBClient.{ AttributeName, ContentConverter, ItemConverter, PrimaryKeyConverter }
import infrastructure.config.ContentDBConfig
import play.api
import services.ContentStore

import scala.collection.JavaConverters._
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success, Try }

class ContentDBClient @Inject()(
    db: DynamoDB,
    contentDBConfig: ContentDBConfig
)(implicit @Named("aws") ec: ExecutionContext)
    extends ContentStore with ContentConverter
        with ItemConverter with PrimaryKeyConverter {

  private val table = db.getTable(contentDBConfig.tableName)

  override def store(content: Content): Future[Unit] = Future {
    Try {
      table.putItem(toNewItem(content.userId, content.newsId))
    } match {
      case Success(_) =>
      case Failure(e) => api.Logger.error("dynamoDB update failed.", e)
    }
  }
}


object ContentDBClient {

  object AttributeName {
    lazy val UserId = "user_id"
    lazy val NewsId = "news_id"
  }

  trait PrimaryKeyConverter {
    def toPrimaryKey(userId: Long): PrimaryKey =
      new PrimaryKey(
        AttributeName.UserId, Long.box(userId)
      )
  }

  trait ItemConverter {
    self: PrimaryKeyConverter =>
    def toNewItem(userId: Long, newsId: String): Item =
      new Item()
          .withPrimaryKey(toPrimaryKey(userId))
          .withString(AttributeName.NewsId, newsId)
  }

  trait ContentConverter {
    def convertToContent(item: Item): Content = Content(
      userId = item.getLong(AttributeName.UserId),
      newsId = item.getString(AttributeName.NewsId)
    )
  }

}
