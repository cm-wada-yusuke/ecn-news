package infrastructure

import domain.news.content.ContentJob
import domain.news.master.MasterJob
import domain.news._
import play.api.libs.functional.syntax._
import play.api.libs.json._


object NewsMessageConverters {

  implicit object OperationFormat extends Format[Operation] {
    override def writes(o: Operation): JsValue = o match {
      case MasterOperation => JsNumber(0)
      case ContentOperation => JsNumber(1)
    }

    override def reads(json: JsValue): JsResult[Operation] = json.validate[Int].map {
      case 0 => MasterOperation
      case 1 => ContentOperation
    }
  }

  implicit val MasterJobFormat: Format[MasterJob] = (
      (JsPath \ "newsId").format[String] and
          (JsPath \ "title").format[String]
      ) (MasterJob.apply, unlift(MasterJob.unapply))

  implicit val ContentJobFormat: Format[ContentJob] = (
      (JsPath \ "userId").format[Long] and
          (JsPath \ "newsId").format[String]
      ) (ContentJob.apply, unlift(ContentJob.unapply))

  implicit object NewsMessageFormat extends Format[NewsMessage] {
    override def writes(o: NewsMessage): JsValue = {
      val operation = Json.obj("operation" -> Json.toJson(o.operation))
      val job = Json.obj("job" -> {
        o.job match {
          case j: MasterJob => Json.toJson(j)(MasterJobFormat)
          case j: ContentJob => Json.toJson(j)(ContentJobFormat)
        }
      })
      operation ++ job
    }

    override def reads(json: JsValue): JsResult[NewsMessage] =
      for {
        operation <- (json \ "operation").validate[Operation]
        job: Job <- operation match {
          case MasterOperation => (json \ "job").validate[MasterJob]
          case ContentOperation => (json \ "job").validate[ContentJob]
        }
      } yield NewsMessage(operation, job)
  }


}
