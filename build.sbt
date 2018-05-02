// *****************************************************************************
// Projects
// *****************************************************************************
fork in run := true

lazy val docs =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin)
    .settings(settings)
    .settings(
      libraryDependencies ++= Seq(
        library.sttp,
        library.scalaCheck % Test,
        library.utest % Test
      ),
      libraryDependencies ++= library.circe
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {

    object Version {
      val scalaCheck = "1.13.5"
      val utest = "0.6.4"
      val circeVersion = "0.9.3"
    }

    val circe = Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % Version.circeVersion)
    val scalaCheck = "org.scalacheck" %% "scalacheck" % Version.scalaCheck
    val utest = "com.lihaoyi" %% "utest" % Version.utest
    val sttp = "com.softwaremill.sttp" %% "core" % "1.1.13"
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
    scalafmtSettings

lazy val commonSettings =
  Seq(
    // scalaVersion from .travis.yml via sbt-travisci
    // scalaVersion := "2.12.4",
    organization := "bert.inc",
    organizationName := "Bert Volders",
    startYear := Some(2018),
    licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-Ypartial-unification",
      "-Ywarn-unused-import"
    ),
    Compile / unmanagedSourceDirectories := Seq((Compile / scalaSource).value),
    Test / unmanagedSourceDirectories := Seq((Test / scalaSource).value),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    wartremoverWarnings in(Compile, compile) ++= Warts.unsafe
  )

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true
  )
