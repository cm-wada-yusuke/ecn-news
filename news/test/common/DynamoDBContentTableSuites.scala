package common

import com.amazonaws.services.dynamodbv2.document.Table
import org.scalatest.{ BeforeAndAfterAll, Suite }

/**
 * DynamoDB会員チェックインボーナステーブルの作成、コンテンツ削除を管理する
 */
trait DynamoDBContentTableSuites extends Suite with BeforeAndAfterAll {

  import DynamoDBContentTable._

  override protected def beforeAll(): Unit = createTableIfNotExist()

  override protected def afterAll(): Unit = deleteContents()
}

object DynamoDBContentTable extends DynamoDBTableHelper {

  import ContentTableDefinition._

  def createTableIfNotExist(): Unit =
    createTableIfNotExist(DescribeRequest, CreateRequest)

  def deleteContents(): Unit =
    deleteContents(ScanRequest, deleteItemRequest)

  override def table: Table = db.getTable(TableName)
}
