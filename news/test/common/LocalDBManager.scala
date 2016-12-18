package common

object LocalDBManager {

  def main(args: Array[String]): Unit = {
    DynamoDBContentTable.createTableIfNotExist()
  }

}
