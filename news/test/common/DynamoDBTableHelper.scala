package common

import com.amazonaws.services.dynamodbv2.document.{ DynamoDB, Table }
import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.services.dynamodbv2.{ AmazonDynamoDB, AmazonDynamoDBClient }

import scala.collection.JavaConversions._

/**
 * DynamoDBのテーブルをテストに利用する場合のSuitesヘルパー。
 */
trait DynamoDBTableHelper {

  def createTableIfNotExist(describe: DescribeTableRequest, create: CreateTableRequest): Unit = try {
    dynamoDB.describeTable(describe)
  } catch {
    case e: ResourceNotFoundException => // テーブルがないときのみ作成する
      dynamoDB.createTable(create)
  }

  def deleteContents(scan: ScanRequest, deleteItem: Map[String, AttributeValue] => DeleteItemRequest): Unit =
    dynamoDB.scan(scan).getItems.toList.foreach { kv => // テーブルの中身のみを削除する
      dynamoDB.deleteItem(deleteItem(kv.toMap))
    }

  def deleteTable(): Unit = table.delete()

  val db = new DynamoDB(dynamoDB)

  def table: Table

  private def dynamoDB: AmazonDynamoDB = {
    val client = new AmazonDynamoDBClient()
    client.setEndpoint("http://localhost:8000")
    client
  }
}
