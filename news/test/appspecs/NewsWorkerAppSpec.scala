package appspecs

import akka.actor.ActorSystem
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.{ Message, ReceiveMessageResult }
import common.{ NewsIntegrationTestHelpers, PlaySpecApplication, WithActorSystem }
import examples.MockWorkerMessageJson
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject._
import play.api.test.Helpers.running
import workers.NewsProcessSupervisor

class NewsWorkerAppSpec extends PlaySpecApplication {

  import NewsIntegrationTestHelpers._

  trait CommonBefore extends WithActorSystem {
    val messageId = "newsWorkerAppSpec"
    val receipt = "djfoseindvf"

    def newValidMessage: Message =
      new Message()
          .withBody(MockWorkerMessageJson.newsContentRegisterMessage)
          .withMessageId(messageId)
          .withReceiptHandle(receipt)

    val mockSQSClient = new AmazonSQSClientDouble(
      receiveMessageStubWithReq = { _ => new ReceiveMessageResult().withMessages(newValidMessage) }
    )
    val application: Application = new GuiceApplicationBuilder()
        .overrides(bind[ActorSystem].toInstance(system))
        .overrides(bind[AmazonSQSClient].toInstance(mockSQSClient))
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
    "エラーが発生せず終了する" in new Context {
      running(application) {
        // dynamoDBへの書き込みが行われたら内容を確認するテストを書く。
      }
    }
  }


}
