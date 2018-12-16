import sbt._

object LibraryVersions {
  object Version {
    val cats = "1.3.1"
    val circe = "0.9.3"
    val kindProjector = "0.9.9"
    val scalaXml = "1.1.1"
    val java8compat = "0.9.0"
    val shapeless = "2.3.3"
    val scalaTest = "3.0.5"
    val scalaCheck = "1.14.0"
    val scalaCheckToolbox = "0.2.5"
  }

  val cats                      = "org.typelevel"          %% "cats-core"              % Version.cats

  val circeCore                 = "io.circe"                %% "circe-core"             % Version.circe
  val circeGeneric              = "io.circe"                %% "circe-generic"          % Version.circe
  val circeGenericExtras        = "io.circe"                %% "circe-generic-extras"   % Version.circe
  val circeParser               = "io.circe"                %% "circe-parser"           % Version.circe
  val circeOptics               = "io.circe"                %% "circe-optics"           % Version.circe
  val circeRefined              = "io.circe"                %% "circe-refined"          % Version.circe
  val circeJava8                = "io.circe"                %% "circe-java8"            % Version.circe


  val scalaxml    = "org.scala-lang.modules" %% "scala-xml"          % Version.scalaXml
  val java8compat = "org.scala-lang.modules" %% "scala-java8-compat" % Version.java8compat
  val shapeless   = "com.chuusai"            %% "shapeless"          % Version.shapeless

  val scalaCheck         = "org.scalacheck" %% "scalacheck"                   % Version.scalaCheck        % Test
  val scalaCheckToolbox  = "com.47deg"      %% "scalacheck-toolbox-datetime"  % Version.scalaCheckToolbox % Test
  val scalaTest          = "org.scalatest"  %% "scalatest"                    % Version.scalaTest         % Test
}