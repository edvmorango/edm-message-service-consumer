name := "edm-message-service-consumer"

version := "0.1"

scalaVersion := "2.12.8"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8")


scalacOptions ++= Seq(
  "-Ypartial-unification",
  "-language:higherKinds",
  "-deprecation",
  "-encoding", "utf-8",
  "-explaintypes",
  "-feature",
  "-language:existentials",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint:infer-any",
  "-Xlint:type-parameter-shadow",
  "-Xlint:unsound-match",
  "-Ywarn-dead-code",
  "-Ywarn-extra-implicit",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-unused:implicits",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates",
  "-Ywarn-value-discard"
)

