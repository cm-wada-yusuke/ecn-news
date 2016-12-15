import com.amazonaws.regions.{ Region, Regions }
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.Message

object SQSMessageSender {

  val messageId = "newsWorkerAppSpec"
  val receipt = "djfoseindvf"

  val sqsClient = new AmazonSQSClient()
  sqsClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1))

  def main(args: Array[String]): Unit = {
    val queueUrl = sys.env("NEWS_QUEUE_URL")
    sqsClient.sendMessage(queueUrl, MockWorkerMessageJson.newsContentRegisterMessage)
  }

}

object MockWorkerMessageJson {

  val newsMasterRegisterMessage: String =
    """
      |{
      |  "operation":0,
      |  "job":{
      |     "newsId": "aeorfq93rghdi"
      |  }
      |}
    """.stripMargin

  val newsContentRegisterMessage: String =
    """
      |{
      |  "operation":1,
      |  "job":{
      |     "userId": 200014235534,
      |     "newsId": "aeorfq93rghdi"
      |  }
      |}
    """.stripMargin

}