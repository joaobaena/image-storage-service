import scala.sys.process._
import BuildEnvPlugin.autoImport._
import sbt.Keys.{name, sbtVersion, scalaVersion, version}
import sbt._
import sbtbuildinfo.BuildInfoKey.action
import sbtbuildinfo.BuildInfoKeys.{
  buildInfoKeys,
  buildInfoOptions,
  buildInfoPackage
}
import sbtbuildinfo.{BuildInfoKey, BuildInfoOption}

object BuildInfo {
  private val now = System.currentTimeMillis()

  lazy val settings: Seq[Def.Setting[_]] = Seq(
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      version,
      scalaVersion,
      sbtVersion,
      commitHash,
      action("gitBranch")("git describe --all".!!.trim),
      action("builtForEnvironment")(buildEnvironment.value),
      action("builtAtString") {
        val dtf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSX")
        dtf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"))
        dtf.format(new java.util.Date(now))
      },
      action("builtAtMillis")(now)
    ),
    buildInfoOptions += BuildInfoOption.ToJson,
    buildInfoPackage := "org.marighella"
  )

}
