import java.util.UUID

import com.amazonaws.regions.{ Region, Regions }
import com.amazonaws.services.sqs.AmazonSQSClient

object SQSMessageSender {

  val messageId = "newsWorkerAppSpec"
  val receipt = "djfoseindvf"

  val sqsClient = new AmazonSQSClient()
  sqsClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1))

  def main(args: Array[String]): Unit = {
    val queueUrl = sys.env("NEWS_QUEUE_URL")
    // まずは100メッセージから
    1 to 100 foreach {
      i => send(queueUrl, i.toLong, UUID.randomUUID().toString)
    }
    sqsClient.shutdown()
  }

  private def send(queueUrl: String, userId: Long, newsId: String): Unit = {
    sqsClient.sendMessage(
      queueUrl,
      MockWorkerMessageJson.newsContentRegisterMessage(userId, newsId)
    )
  }

  object MockWorkerMessageJson {

    def newsContentRegisterMessage(userId: Long, newsId: String): String =
      s"""
         |{
         |  "operation":1,
         |  "job":{
         |     "userId": ${ userId },
         |     "newsId": "${ newsId }"
         |  }
         |}
    """.stripMargin

  }

}

