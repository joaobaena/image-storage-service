import Common._
import Dependencies._

lazy val root =
  (project in file(".")).commonSettings.scalafmtSettings.dockerSettings //.coverageSettings
    .enablePlugins(BuildInfoPlugin)
    .enablePlugins(AshScriptPlugin)
    .settings(
      name := "image-store-service",
      libraryDependencies ++= akka ++ monix ++ cats ++ awsS3 ++ pureConfig ++ circe ++ logging ++ scalaTest
    )
