sbtPlugin := true

name := "sbt-velocity"

organization := "cchantep"

version := "0.1"

libraryDependencies += "org.apache.velocity" % "velocity-engine-core" % "2.3"

// Scripted test
pluginCrossBuild / sbtVersion := "1.3.13"

enablePlugins(SbtPlugin)

scriptedBufferLog := false

scriptedLaunchOpts ++= Seq(
  "-Xmx1024M",
  s"-Dsbt-velocity.version=${version.value}"
)

scripted := scripted.dependsOn(publishLocal).evaluated

publishTo in ThisBuild := sys.env.get("REPO_PATH").map { path =>
  import Resolver.ivyStylePatterns

  val repoDir = new java.io.File(path)

  Resolver.file("repo", repoDir)
}
