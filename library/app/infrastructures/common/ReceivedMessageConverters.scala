package infrastructures.common

import com.amazonaws.services.sqs.model.Message
import domain.common.{ MessageToken, ProcessableMessage, ReceivedMessage, UnprocessableMessage }
import play.api.libs.json.{ JsError, JsSuccess, Json, Reads }

object ReceivedMessageConverters {

  def toReceivedMessage[T](message: Message)(implicit reads: Reads[T]): ReceivedMessage[T] = {
    val json = Json.parse(message.getBody)
    val result = Json.fromJson(json)
    val token = MessageToken(message.getReceiptHandle)
    result match {
      case JsSuccess(t, _) => ProcessableMessage(t, token)
      case JsError(e) =>
        val errorMessage = s"messageId: ${ message.getMessageId }, reason: ${ Json.stringify(JsError.toJson(e)) }"
        UnprocessableMessage(new IllegalArgumentException(errorMessage), token)
    }
  }
}
