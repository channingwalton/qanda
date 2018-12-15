import sbt._

object LibraryVersions {
  object Version {
      val cats = "0.9.0"
      val scalaXml = "1.1.1"
      val java8compat = "0.8.0"
      val shapeless = "2.3.2"
  }

  val cats        = "org.typelevel"          %% "cats"               % Version.cats
  val scalaxml    = "org.scala-lang.modules" %% "scala-xml"          % Version.scalaXml
  val java8compat = "org.scala-lang.modules" %% "scala-java8-compat" % Version.java8compat
  val shapeless   = "com.chuusai"            %% "shapeless"          % Version.shapeless
}