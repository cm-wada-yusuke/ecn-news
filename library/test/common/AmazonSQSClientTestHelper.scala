package common

import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.{ ReceiveMessageRequest, ReceiveMessageResult }

trait AmazonSQSClientTestHelper {

  class AmazonSQSClientDouble(
      receiveMessageStubWithReq: ReceiveMessageRequest => ReceiveMessageResult = AmazonSQSClientDouble.defaultReceiveMessageStubWithReq,
      receiveMessageStubWithStr: String => ReceiveMessageResult = AmazonSQSClientDouble.defaultReceiveMessageStubWithStr,
      deleteMessageStub: (String, String) => Unit = AmazonSQSClientDouble.defaultDeleteMessageStub
  ) extends AmazonSQSClient {

    override def receiveMessage(receiveMessageRequest: ReceiveMessageRequest): ReceiveMessageResult =
      receiveMessageStubWithReq(receiveMessageRequest)

    override def receiveMessage(queueUrl: String): ReceiveMessageResult =
      receiveMessageStubWithStr(queueUrl)

    override def deleteMessage(queueUrl: String, receiptHandle: String): Unit =
      deleteMessageStub(queueUrl, receiptHandle)
  }

  object AmazonSQSClientDouble {

    import scala.collection.JavaConversions._
    import scala.Function._

    private def defaultReceiveMessageStubWithReq: ReceiveMessageRequest => ReceiveMessageResult =
      const(new ReceiveMessageResult().withMessages(Nil))

    private def defaultReceiveMessageStubWithStr: String => ReceiveMessageResult =
      const(new ReceiveMessageResult().withMessages(Nil))

    private def defaultDeleteMessageStub: (String, String) => Unit = { (_, _) => () }
  }

}
