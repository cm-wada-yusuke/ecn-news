package examples

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
