organization := "channing"

name := "qanda"

version := "0.0.1"

scalaVersion := "2.12.0"

resolvers ++= Seq("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "releases" at "https://oss.sonatype.org/content/repositories/releases",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
)

scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-Ydelambdafy:method",
  "-Ybackend:GenBCode",
  "-Yopt:l:classpath",
  "-unchecked",
  "-deprecation",
  "-language:_",
  "-Xfatal-warnings",
  "-Xlint")

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= Seq (
  "org.typelevel" %% "cats" % "0.6.0",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.5.0" % "compile",
  "com.chuusai" %% "shapeless" % "2.2.5" % "compile"
)
