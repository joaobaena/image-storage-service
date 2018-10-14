import Common._
import Dependencies._

lazy val root =
  (project in file(".")).commonSettings.scalafmtSettings.dockerSettings
    .enablePlugins(BuildInfoPlugin)
    .settings(
      name := "image-store-service",
      libraryDependencies ++= akka ++ monix ++ pureConfig ++ circe ++ logging ++ scalaTest
    )
