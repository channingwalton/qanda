// Code formatter. See https://github.com/lucidsoftware/neo-sbt-scalafmt
addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.14")

// Used to find outdated dependencies - See https://github.com/rtimush/sbt-updates
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.3")

// Scala code linting tool - See http://www.wartremover.org
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.2.1")

// Speeds up retrieval of dependencies - See https://github.com/coursier/coursier
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC13")