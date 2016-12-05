organization := "channing"

name := "qanda"

version := "0.0.1"

scalaVersion := "2.12.1"
scalaBinaryVersion := "2.12"

resolvers ++= Seq("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "releases" at "https://oss.sonatype.org/content/repositories/releases",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
)

scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-Xfatal-warnings",
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import")

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= Seq (
  "org.typelevel" %% "cats" % "0.8.1",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.5",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.8.0" % "compile",
  "com.chuusai" %% "shapeless" % "2.3.2" % "compile"
)
