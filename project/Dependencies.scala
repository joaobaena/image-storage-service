import sbt._

object Dependencies {

  object Version {
    val akka = "2.5.17"
    val akkaHttp = "10.1.5"
    val monix = "2.3.0"
    val pureConfig = "0.9.0"
    val circe = "0.8.0"
    val logback = "1.2.3"
    val scalaTest = "3.0.4"
  }

  val akka = Seq(
    "com.typesafe.akka" %% "akka-stream" % Version.akka,
    "com.typesafe.akka" %% "akka-slf4j" % Version.akka,
    "com.typesafe.akka" %% "akka-stream-testkit" % Version.akka,
    "com.typesafe.akka" %% "akka-http" % Version.akkaHttp,
    "com.typesafe.akka" %% "akka-http-testkit" % Version.akkaHttp % Test
  )

  val monix = Seq(
    "io.monix" %% "monix-execution" % Version.monix,
    "io.monix" %% "monix-eval" % Version.monix,
    "io.monix" %% "monix-cats" % Version.monix
  )

  val pureConfig = Seq(
    "com.github.pureconfig" %% "pureconfig" % Version.pureConfig
  )

  val circe = Seq(
    "io.circe" %% "circe-core" % Version.circe,
    "io.circe" %% "circe-generic" % Version.circe,
    "io.circe" %% "circe-parser" % Version.circe,
    "io.circe" %% "circe-generic-extras" % Version.circe,
    "de.heikoseeberger" %% "akka-http-circe" % "1.18.1"
  )

  val logging = Seq(
    "ch.qos.logback" % "logback-classic" % Version.logback,
    "net.logstash.logback" % "logstash-logback-encoder" % "4.11"
  )

  val scalaTest = Seq(
    "org.scalatest" %% "scalatest" % "3.0.4" % Test
  )
}