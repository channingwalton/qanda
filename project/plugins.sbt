resolvers ++= Seq("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "releases" at "https://oss.sonatype.org/content/repositories/releases",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
)

addSbtPlugin("com.gilt" % "sbt-dependency-graph-sugar" % "0.7.5-1")
