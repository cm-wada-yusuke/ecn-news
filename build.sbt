name := """ecn-worker"""

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  scalaSource in Compile <<= (baseDirectory in Compile) (_ / "app"),
  scalaSource in Test <<= (baseDirectory in Test) (_ / "test"),
  resolvers += "maven" at "https://mvnrepository.com/"
)

lazy val root = (project in file("."))
    .aggregate(news, library)

lazy val news = project.in(file("./news"))
    .settings(commonSettings: _*)
    .settings(
      name := "ecn-worker-news",
      version := "0.1.0"
    )
    .settings(libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-java-sdk" % "1.10.1",
      "org.scalatest" % "scalatest_2.11" % "3.0.1"
    ))
    .dependsOn(library % "test->test;compile->compile")


lazy val library = project.in(file("./library"))
    .settings(commonSettings: _*)
    .settings(libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-java-sdk" % "1.10.1",
      "org.scalatest" % "scalatest_2.11" % "3.0.1"
    ))
    .settings(
      name := "ecn-worker-library",
      version := "0.1.0"
    )
