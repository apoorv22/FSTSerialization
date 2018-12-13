name := "FSTSerialization"

version := "0.1"

scalaVersion := "2.12.8"

val redis = "net.debasishg" %% "redisclient" % "3.8"

lazy val pmmlold = project
  .in(file("pmml-old"))
  .settings(name := "pmml-old",
    libraryDependencies ++= Seq("org.jpmml" % "pmml-model" % "1.4.6","org.jpmml" % "pmml-evaluator" % "1.4.2"),
    dependencyOverrides ++= Seq("org.jpmml" % "pmml-model" % "1.4.6")
  )
  .dependsOn(common)

lazy val pmmlnew = project
  .in(file("pmml-new"))
  .settings(name := "pmml-new", libraryDependencies ++= Seq("org.jpmml" % "pmml-model" % "1.4.7","org.jpmml" % "pmml-evaluator" % "1.4.3"),
    dependencyOverrides ++= Seq("org.jpmml" % "pmml-model" % "1.4.7")
  )
 .dependsOn(common)

lazy val common = project
  .in(file("common"))
  .settings(
    name := "common",
    libraryDependencies ++= Seq("de.ruedigermoeller" % "fst" % "2.56", redis)
  )

lazy val root =
  project
    .in(file("."))
    .aggregate(pmmlold, pmmlnew, common)
