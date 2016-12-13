name := """ecn-worker"""

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  scalaSource in Compile <<= (baseDirectory in Compile) (_ / "app"),
  scalaSource in Test <<= (baseDirectory in Test) (_ / "test"),
  resolvers += "maven" at "https://mvnrepository.com/",
  javaOptions ++= sys.process.javaVmArguments.filter(
    a => Seq("-Xmx", "-Xms", "-XX").exists(a.startsWith)
  )
)

lazy val root = (project in file("."))
    .aggregate(news, library)

lazy val library = project.in(file("./library")).enablePlugins(PlayScala)
    .settings(commonSettings: _*)
    .settings(libraryDependencies ++= Seq(
      "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % "test",
      "com.typesafe.akka" %% "akka-testkit" % "2.4.14" % "test",
      "com.amazonaws" % "aws-java-sdk" % "1.10.1",
      "org.scalatest" % "scalatest_2.11" % "3.0.1"
    ))
    .settings(
      name := "ecn-worker-library",
      version := "0.1.0"
    )


lazy val news = project.in(file("./news"))
    .enablePlugins(PlayScala)
    .enablePlugins(DockerPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "ecn-worker-news",
      version := "0.1.0"
    )
    .settings(libraryDependencies ++= Seq(
    ))
    .settings(
      maintainer in Docker := "Yusuke Wada <wada.yusuke@classmethod.jp>",
      dockerExposedPorts in Docker := Seq(9000, 9443),
      dockerRepository := Some("cmwadayusuke")
    )
    .dependsOn(library % "test->test;compile->compile")


// Docker settings




