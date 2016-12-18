package common

import com.amazonaws.services.dynamodbv2.model._
import infrastructure.ContentDBClient.AttributeName

import scala.collection.JavaConverters._

object ContentTableDefinition {


  val Attributes = List(
    new AttributeDefinition(AttributeName.UserId, "N")
  )
  val KeySchema = new KeySchemaElement(AttributeName.UserId, KeyType.HASH)

  private val readCapacityUnits: Long = 5L
  private val writeCapacityUnits: Long = 1L
  val Throughput = new ProvisionedThroughput(readCapacityUnits, writeCapacityUnits)

  val TableName = "content-metadata"
  val CreateRequest: CreateTableRequest = newCreateRequest(TableName)
  val DescribeRequest: DescribeTableRequest = newDescribeRequest(TableName)
  val ScanRequest: ScanRequest = newScanRequest(TableName)

  def deleteItemRequest(kv: Map[String, AttributeValue]): DeleteItemRequest =
    new DeleteItemRequest(
      TableName,
      Map(AttributeName.UserId
          -> new AttributeValue().withN(kv(AttributeName.UserId).getN)).asJava
    )

  def newCreateRequest(tableName: String): CreateTableRequest =
    new CreateTableRequest()
        .withTableName(tableName)
        .withAttributeDefinitions(Attributes.asJava)
        .withKeySchema(KeySchema)
        .withProvisionedThroughput(Throughput)

  def newDescribeRequest(tableName: String): DescribeTableRequest =
    new DescribeTableRequest().withTableName(tableName)

  def newScanRequest(tableName: String): ScanRequest =
    new ScanRequest().withTableName(tableName)
}
