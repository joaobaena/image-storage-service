import sbt.Keys.{version, _}
import sbt._
import com.lucidchart.sbt.scalafmt.ScalafmtCorePlugin.autoImport._
import com.lucidchart.sbt.scalafmt.ScalafmtPlugin
import com.typesafe.sbt.SbtNativePackager.autoImport.maintainer
import com.typesafe.sbt.packager.archetypes.JavaServerAppPackaging
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}
import org.scalastyle.sbt.ScalastylePlugin.autoImport.scalastyleFailOnError

object Common {
  val commonScalacOptions = Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-Ywarn-dead-code",
    "-Ywarn-infer-any",
    "-Ywarn-unused-import",
    "-Ypartial-unification",
    "-Xlint",
    "-Xlint:-adapted-args,-package-object-classes"
  )
  val productionScalacOptions = Seq("-Xfatal-warnings")

  implicit class ProjectFrom(project: Project) {

    def disablePublish: Project =
      project
        .settings(publish := {}, publishLocal := {})

    def scalafmtSettings: Project =
      project
        .enablePlugins(ScalafmtPlugin)
        .settings(scalafmtOnCompile := true)

    def commonSettings: Project = project.settings(
      version := "0.1",
      organization := "paulo",
      scalaVersion := "2.12.6",
      scalacOptions ++= commonScalacOptions,
      javacOptions ++= Seq("-encoding", "UTF-8"),
      BuildInfo.settings,
      sources in (Compile, doc) := Seq.empty,
      publishArtifact in (Compile, packageDoc) := false,
      cancelable in Global := true,
      scalastyleFailOnError := true
    )

    def dockerSettings: Project = {
      project
        .enablePlugins(JavaServerAppPackaging)
        .settings(
          dockerBaseImage := "openjdk:jre-alpine",
          dockerRepository := Some("520737409608.dkr.ecr.us-east-1.amazonaws.com/marighella"),
          dockerUpdateLatest := true,
          maintainer in Docker := "Paulo Baena <jpbaena@gmail.com>",
          version in Docker := version.value,
          dockerCommands := dockerCommands.value.map {
            case ExecCmd("ENTRYPOINT", args @ _*) => ExecCmd("CMD", args.mkString)
            case x => x
          }.filterNot {
            case ExecCmd("CMD", args @_*) => args.isEmpty
            case _ => false
          }
        )
    }

  }
}
