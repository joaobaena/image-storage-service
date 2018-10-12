import scala.sys.process._
import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

/** sets the build environment */
object BuildEnvPlugin extends AutoPlugin {
  object BuildEnvironment extends Enumeration {
    val Development, Production = Value

    def current: Value =
      sys.props
        .get("build_env")
        .orElse(sys.env.get("BUILD_ENV"))
        .map(_.toLowerCase)
        .flatMap {
          case "prod" | "production"           => Some(Production)
          case "dev" | "devel" | "development" => Some(Development)
          case _                               => None
        }
        .getOrElse(Development)
  }

  object autoImport {
    val commitHash = settingKey[String]("Long SHA of the latest git commit")
    val buildEnvironment =
      settingKey[BuildEnvironment.Value]("Current build environment")

    def ifDevelopmentOrElse[A](f: => A, default: => A): Def.Initialize[A] =
      Def.setting {
        buildEnvironment.value match {
          case BuildEnvironment.Development => f
          case _                            => default
        }
      }
    def ifDevelopmentOrNil[A](f: => Seq[A]): Def.Initialize[Seq[A]] =
      ifDevelopmentOrElse(f, Nil)
    def ifDevelopmentOrNone[A](f: => A): Def.Initialize[Option[A]] =
      ifDevelopmentOrElse(Some(f), None)

    def ifProductionOrElse[A](f: => A, default: => A): Def.Initialize[A] =
      Def.setting {
        buildEnvironment.value match {
          case BuildEnvironment.Production => f
          case _                           => default
        }
      }
    def ifProductionOrNil[A](f: => Seq[A]): Def.Initialize[Seq[A]] =
      ifProductionOrElse(f, Nil)
    def ifProductionOrNone[A](f: => A): Def.Initialize[Option[A]] =
      ifProductionOrElse(Some(f), None)
  }

  import autoImport._

  override def trigger: PluginTrigger = AllRequirements
  override def requires: JvmPlugin.type = JvmPlugin

  override def projectSettings: Seq[Setting[_]] =
    Seq(
      commitHash := "git rev-parse HEAD".!!.trim,
      buildEnvironment := BuildEnvironment.current,
      onLoadMessage := {
        s"""|${onLoadMessage.value}
            |Running in build environment: ${buildEnvironment.value}
            |""".stripMargin
      }
    )
}
