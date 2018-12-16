import LibraryVersions._

name := "questionnaire"

addCompilerPlugin("org.spire-math" %% "kind-projector" % LibraryVersions.Version.kindProjector)

libraryDependencies ++= Seq(
  cats,
  circeCore,
  circeGeneric,
  circeGenericExtras,
  circeParser,
  circeOptics,
  circeRefined,
  circeJava8,
  shapeless,
  scalaTest,
  scalaCheck,
  scalaCheckToolbox
)
