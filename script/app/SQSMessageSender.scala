import java.util.UUID

import com.amazonaws.regions.{ Region, Regions }
import com.amazonaws.services.sqs.AmazonSQSClient

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

object SQSMessageSender {

  val queueUrl: String = sys.env("NEWS_QUEUE_URL")

  val sqsClient = new AmazonSQSClient()
  sqsClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1))


  def main(args: Array[String]): Unit = {
    //    1 to 10 foreach {
    //      i => send((i - 1) * 1000 + 1 to i * 1000)
    //    }
    send(7004 to 10000)
  }

  private def send(userIdRange: Range): Future[Unit] = Future {
    userIdRange.foreach { i =>
      sqsClient.sendMessage(
        queueUrl,
        MockWorkerMessageJson.newsContentRegisterMessage(i.toLong, UUID.randomUUID().toString)
      )
    }
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

