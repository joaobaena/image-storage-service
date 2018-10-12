import sbt._

object Dependencies {

  object Version {
    val akkaHttp = "10.1.5"
    val pureConfig = ""
    val logback = "1.2.3"
    val circe = ""

  }

  val akkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http" % Version.akkaHttp,
    "com.typesafe.akka" %% "akka-http-testkit" % Version.akkaHttp % Test
  )

  val logging = Seq(
    "ch.qos.logback" % "logback-classic" % Version.logback,
    "net.logstash.logback" % "logstash-logback-encoder" % "4.11"
  )
}