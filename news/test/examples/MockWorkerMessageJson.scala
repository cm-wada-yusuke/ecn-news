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
