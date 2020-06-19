val scala_2_11             = "2.11.12"
val scala_2_12             = "2.12.8"
val scala_2_13             = "2.13.1"
val mainScalaVersion       = scala_2_13
val supportedScalaVersions = Seq(scala_2_11, scala_2_12, scala_2_13)

lazy val baseSettings = Seq(
  organization := "pl.iterators",
  organizationName := "Iterators",
  organizationHomepage := Some(url("https://iterato.rs")),
  homepage := Some(url("https://github.com/theiterators/kebs")),
  scalaVersion := mainScalaVersion,
  scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-encoding", "utf8"),
  scalafmtVersion := "1.3.0",
  scalafmtOnCompile := true
)

lazy val commonMacroSettings = baseSettings ++ Seq(
  libraryDependencies += "org.scala-lang" % "scala-reflect"  % scalaVersion.value,
  libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
)

lazy val metaSettings = commonSettings ++ Seq(
  scalacOptions ++= paradiseFlag(scalaVersion.value),
  libraryDependencies ++= paradisePlugin(scalaVersion.value)
)

lazy val publishToNexus = publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

lazy val crossBuildSettings = Seq(crossScalaVersions := scala_2_13 +: supportedScalaVersions, releaseCrossBuild := true)

lazy val publishSettings = Seq(
  publishToNexus,
  publishMavenStyle := true,
  pomIncludeRepository := const(true),
  licenses := Seq("MIT License" -> url("http://opensource.org/licenses/MIT")),
  developers := List(
    Developer(id = "mrzeznicki",
              name = "Marcin Rzeźnicki",
              email = "mrzeznicki@iterato.rs",
              url = url("https://github.com/marcin-rzeznicki")),
    Developer(id = "jborkowski", name = "Jonatan Borkowski", email = "jborkowski@iterato.rs", url = url("https://github.com/jborkowski"))
  ),
  scmInfo := Some(
    ScmInfo(browseUrl = url("https://github.com/theiterators/kebs"), connection = "scm:git:https://github.com/theiterators/kebs.git")),
  useGpg := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
) ++ crossBuildSettings

lazy val noPublishSettings =
  Seq(
    publishToNexus /*must be set for sbt-release*/,
    publishArtifact := false,
    releaseCrossBuild := false,
    releasePublishArtifactsAction := {
      val projectName = name.value
      streams.value.log.warn(s"Publishing for $projectName is turned off")
    }
  )

def optional(dependency: ModuleID) = dependency % "provided"
def sv[A](scalaVersion: String, scala2_11Version: => A, scala2_12Version: => A, scala2_13Version: => A) =
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, 13)) => scala2_13Version
    case Some((2, 12)) => scala2_12Version
    case Some((2, 11)) => scala2_11Version
    case _ =>
      throw new IllegalArgumentException(s"Unsupported Scala version $scalaVersion")
  }

def paradiseFlag(scalaVersion: String): Seq[String] =
  if (scalaVersion == scala_2_11 || scalaVersion == scala_2_12)
    Seq.empty
  else
    Seq("-Ymacro-annotations")

def paradisePlugin(scalaVersion: String): Seq[ModuleID] =
  if (scalaVersion == scala_2_11 || scalaVersion == scala_2_12)
    Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full))
  else
    Seq.empty

val scalaTest     = "org.scalatest" %% "scalatest" % "3.2.0"
val slick         = "com.typesafe.slick" %% "slick" % "3.3.2"
val optionalSlick = optional(slick)
val slickPg       = "com.github.tminglei" %% "slick-pg" % "0.19.0"
val sprayJson     = "io.spray" %% "spray-json" % "1.3.5"
val playJson      = "com.typesafe.play" %% "play-json" % "2.7.4"

val enumeratumVersion         = "1.6.1"
val enumeratumPlayJsonVersion = "1.5.16"
val enumeratum                = "com.beachape" %% "enumeratum" % enumeratumVersion
def enumeratumInExamples = {
  val playJsonSupport = "com.beachape" %% "enumeratum-play-json" % enumeratumPlayJsonVersion
  Seq(enumeratum, playJsonSupport)
}
val optionalEnumeratum = optional(enumeratum)

val akkaVersion       = "2.5.31"
val akkaHttpVersion   = "10.1.12"
val akkaStream        = "com.typesafe.akka" %% "akka-stream" % akkaVersion
val akkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion
val akkaHttp          = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
val akkaHttpTestkit   = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion
def akkaHttpInExamples = {
  val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
  Seq(akkaStream, akkaHttp, akkaHttpSprayJson)
}
def akkaHttpInBenchmarks = akkaHttpInExamples :+ akkaHttpTestkit

lazy val commonSettings = baseSettings ++ Seq(
  scalacOptions ++= Seq("-language:experimental.macros"),
  (scalacOptions in Test) ++= Seq("-Ymacro-debug-lite", "-Xlog-implicits"),
  libraryDependencies += scalaTest % "test",
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

lazy val slickSettings = commonSettings ++ Seq(
  libraryDependencies += slick,
  libraryDependencies += slickPg % "test",
  libraryDependencies += optionalEnumeratum
)

lazy val macroUtilsSettings = commonMacroSettings ++ Seq(
  libraryDependencies += optionalEnumeratum
)

lazy val sprayJsonMacroSettings = commonMacroSettings ++ Seq(
  libraryDependencies += sprayJson
)

lazy val sprayJsonSettings = commonSettings ++ Seq(
  libraryDependencies += optionalEnumeratum
)

lazy val playJsonSettings = commonSettings ++ Seq(
  libraryDependencies += playJson
)

lazy val akkaHttpSettings = commonSettings ++ Seq(
  libraryDependencies ++= sv(scalaVersion.value, Seq(akkaStream, akkaHttp), Seq(akkaHttp), Seq(akkaHttp)),
  libraryDependencies += akkaStreamTestkit % "test",
  libraryDependencies += akkaHttpTestkit   % "test",
  libraryDependencies += optionalEnumeratum
)

lazy val taggedSettings = commonSettings ++ Seq(
  libraryDependencies += optionalSlick
)

lazy val examplesSettings = commonSettings ++ Seq(
  libraryDependencies += slickPg,
  libraryDependencies ++= enumeratumInExamples,
  libraryDependencies ++= akkaHttpInExamples,
  libraryDependencies ++= paradisePlugin(scalaVersion.value),
  scalacOptions ++= paradiseFlag(scalaVersion.value)
)

lazy val benchmarkSettings = commonSettings ++ Seq(
  libraryDependencies += scalaTest,
  libraryDependencies += enumeratum,
  libraryDependencies ++= akkaHttpInBenchmarks
)

lazy val taggedMetaSettings = metaSettings ++ Seq(
  libraryDependencies += optional(sprayJson)
)

lazy val macroUtils = project
  .in(file("macro-utils"))
  .settings(macroUtilsSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "macro-utils",
    description := "Macros supporting Kebs library",
    moduleName := "kebs-macro-utils",
    crossScalaVersions := supportedScalaVersions
  )

lazy val slickSupport = project
  .in(file("slick"))
  .dependsOn(macroUtils)
  .settings(slickSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "slick",
    description := "Library to eliminate the boilerplate code that comes with the use of Slick",
    moduleName := "kebs-slick",
    crossScalaVersions := supportedScalaVersions
  )

lazy val sprayJsonMacros = project
  .in(file("spray-json-macros"))
  .dependsOn(macroUtils)
  .settings(sprayJsonMacroSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "spray-json-macros",
    description := "Automatic generation of Spray json formats for case-classes - macros",
    moduleName := "kebs-spray-json-macros",
    crossScalaVersions := supportedScalaVersions
  )

lazy val sprayJsonSupport = project
  .in(file("spray-json"))
  .dependsOn(sprayJsonMacros)
  .settings(sprayJsonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "spray-json",
    description := "Automatic generation of Spray json formats for case-classes",
    moduleName := "kebs-spray-json",
    crossScalaVersions := supportedScalaVersions
  )

lazy val playJsonSupport = project
  .in(file("play-json"))
  .dependsOn(macroUtils)
  .settings(playJsonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "play-json",
    description := "Automatic generation of Play json formats for case-classes",
    moduleName := "kebs-play-json",
    crossScalaVersions := supportedScalaVersions
  )

lazy val akkaHttpSupport = project
  .in(file("akka-http"))
  .dependsOn(macroUtils)
  .settings(akkaHttpSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "akka-http",
    description := "Automatic generation of akka-http deserializers for 1-element case classes",
    moduleName := "kebs-akka-http",
    crossScalaVersions := supportedScalaVersions
  )

lazy val tagged = project
  .in(file("tagged"))
  .settings(taggedSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "tagged",
    description := "Representation of tagged types",
    moduleName := "kebs-tagged",
    crossScalaVersions := supportedScalaVersions
  )

lazy val taggedMeta = project
  .in(file("tagged-meta"))
  .dependsOn(macroUtils, tagged, sprayJsonSupport % "test -> test")
  .settings(taggedMetaSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "tagged-meta",
    description := "Representation of tagged types - code generation based on scala-meta",
    moduleName := "kebs-tagged-meta",
    crossScalaVersions := supportedScalaVersions
  )

lazy val examples = project
  .in(file("examples"))
  .dependsOn(slickSupport, sprayJsonSupport, playJsonSupport, akkaHttpSupport, taggedMeta)
  .settings(examplesSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(
    name := "examples",
    moduleName := "kebs-examples"
  )

lazy val benchmarks = project
  .in(file("benchmarks"))
  .dependsOn(sprayJsonSupport)
  .enablePlugins(JmhPlugin)
  .settings(benchmarkSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(
    name := "benchmarks",
    moduleName := "kebs-benchmarks"
  )

import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._

lazy val kebs = project
  .in(file("."))
  .aggregate(
    tagged,
    macroUtils,
    slickSupport,
    sprayJsonMacros,
    sprayJsonSupport,
    playJsonSupport,
    akkaHttpSupport,
    taggedMeta
  )
  .settings(baseSettings: _*)
  .settings(
    name := "kebs",
    description := "Library to eliminate the boilerplate code",
    publishToNexus, /*must be set for sbt-release*/
    releaseCrossBuild := false,
    releaseProcess := Seq(
      checkSnapshotDependencies,
      inquireVersions,
      releaseStepCommandAndRemaining("+publishLocalSigned"),
      releaseStepCommandAndRemaining("+clean"),
      releaseStepCommandAndRemaining("+test"),
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("+publishSigned"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    ),
    publishArtifact := false,
    crossScalaVersions := Nil
  )
