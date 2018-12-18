// See https://github.com/coursier/coursier
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.1")

// See http://scalameta.org/scalafmt/#sbt
addSbtPlugin("com.geirsson"      % "sbt-scalafmt"    % "1.5.1")

// See http://www.wartremover.org/doc/install-setup.html
addSbtPlugin("org.wartremover"   % "sbt-wartremover" % "2.3.7")

// See http://www.scalastyle.org/sbt.html
// NOTE: despite already using scapegoat the recommendation is to have both, as worst case the same issue is detected twice
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

// See https://github.com/albuch/sbt-dependency-check
addSbtPlugin("net.vonbuchholtz" % "sbt-dependency-check" % "0.2.4")

// See https://github.com/rtimush/sbt-updates
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")

// See https://github.com/sbt/sbt-native-packager
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.4")

// See https://github.com/jrudolph/sbt-dependency-graph
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.0")

// See https://github.com/sbt/sbt-release
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.10")