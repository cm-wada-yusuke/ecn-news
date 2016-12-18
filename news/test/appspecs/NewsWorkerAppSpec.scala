package appspecs

import akka.actor.ActorSystem
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.{ Message, ReceiveMessageResult }
import common.{ DynamoDBContentTableSuites, NewsIntegrationTestHelpers, PlaySpecApplication, WithActorSystem }
import examples.MockWorkerMessageJson
import infrastructure.ContentDBClient.AttributeName
import play.api.Application
import play.api.inject._
import play.api.inject.guice.GuiceApplicationBuilder
import workers.NewsProcessSupervisor

class NewsWorkerAppSpec extends PlaySpecApplication with DynamoDBContentTableSuites {

  import NewsIntegrationTestHelpers._
  import common.DynamoDBContentTable._

  trait CommonBefore extends WithActorSystem {
    val messageId = "newsWorkerAppSpec"
    val receipt = "djfoseindvf"
    val userId = 12345678L
    val newsId = "rthyewfgadsjkg"

    def newValidMessage: Message =
      new Message()
          .withBody(MockWorkerMessageJson.newsContentRegisterMessage(userId, newsId))
          .withMessageId(messageId)
          .withReceiptHandle(receipt)

    val mockSQSClient = new AmazonSQSClientDouble(
      receiveMessageStubWithReq = { _ => new ReceiveMessageResult().withMessages(newValidMessage) }
    )
    val application: Application = new GuiceApplicationBuilder()
        .overrides(bind[ActorSystem].toInstance(system))
        .overrides(bind[AmazonSQSClient].toInstance(mockSQSClient))
        .overrides(bind[DynamoDB].toInstance(db))
        .build()
  }

  class Context extends CommonBefore

  "アプリ起動時" should {
    "ワーカーが起動している" in new Context {
      running(application) {
        val actor = retrieveSupervisorActorRef
        actor ! NewsProcessSupervisor.Ping
        expectMsg(NewsProcessSupervisor.Pong)
      }
    }
  }

  "スケジュール実行時" should {
    "DynamoDBにデータが書き込まれたことを確認できる" in new Context {
      running(application) {
        awaitCond {
          val item = table.getItem(AttributeName.UserId, userId)
          item.getString(AttributeName.NewsId) == newsId
        }
      }
    }
  }


}
