import sbt._

object Dependencies {
  object Version {
    val akka = "2.5.19"
    val akkaHttp = "10.1.5"
    val alpakka = "1.0-M1"
    val monix = "3.0.0-RC2"
    val cats = "1.5.0"
    val awsS3 = "1.8.10.2"
    val pureConfig = "0.9.0"
    val circe = "0.11.0"
    val logback = "1.2.3"
    val scalaTest = "3.0.5"
  }

  val akka = Seq(
    "com.typesafe.akka" %% "akka-stream" % Version.akka,
    "com.typesafe.akka" %% "akka-slf4j" % Version.akka,
    "com.typesafe.akka" %% "akka-stream-testkit" % Version.akka,
    "com.typesafe.akka" %% "akka-http" % Version.akkaHttp,
    "com.typesafe.akka" %% "akka-http-testkit" % Version.akkaHttp % Test,
    "com.lightbend.akka" %% "akka-stream-alpakka-s3" % Version.alpakka
  )

  val monix = Seq(
    "io.monix" %% "monix" % Version.monix
  )

  val cats = Seq(
    "org.typelevel" %% "cats-core" % Version.cats,
    "org.typelevel" %% "cats-mtl-core" % "0.4.0",
    "org.typelevel" %% "cats-effect" % "1.2.0",
    )

  val awsS3 = Seq(
    "com.amazonaws" % "aws-java-sdk" % Version.awsS3
  )
  val pureConfig = Seq(
    "com.github.pureconfig" %% "pureconfig" % Version.pureConfig
  )

  val circe = Seq(
    "io.circe" %% "circe-core" % Version.circe,
    "io.circe" %% "circe-generic" % Version.circe,
    "io.circe" %% "circe-parser" % Version.circe,
    "io.circe" %% "circe-generic-extras" % Version.circe,
    "de.heikoseeberger" %% "akka-http-circe" % "1.23.0"
  )

  val logging = Seq(
    "ch.qos.logback" % "logback-classic" % Version.logback,
    "net.logstash.logback" % "logstash-logback-encoder" % "4.11"
  )

  val scalaTest = Seq(
    "org.scalatest" %% "scalatest" % "3.0.4" % Test
  )
}