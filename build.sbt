sbtPlugin := true

name := "sbt-velocity"

organization := "cchantep"

version := "0.3"

libraryDependencies += "org.apache.velocity" % "velocity-engine-core" % "2.4.1"

ThisBuild / Compile / javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

ThisBuild / Compile / doc / javacOptions --= Seq("-source", "1.8", "-target", "1.8")

// Scripted test
crossSbtVersions := Vector("1.12.3", "2.0.1")

scalaVersion := {
  val v = sbtVersion.value

  if (v.startsWith("2.")) {
    "3.8.4"
  } else {
    "2.12.20"
  }
}

enablePlugins(ScriptedPlugin)

scriptedBufferLog := false

scriptedLaunchOpts ++= Seq(
  "-Xmx1024M",
  s"-Dsbt-velocity.version=${version.value}"
)

scripted := scripted.dependsOn(publishLocal).evaluated

ThisBuild / publishTo := sys.env.get("REPO_PATH").map { path =>
  import Resolver.ivyStylePatterns

  val repoDir = new java.io.File(path)

  Resolver.file("repo", repoDir)
}
