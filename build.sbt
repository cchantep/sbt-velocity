sbtPlugin := true

name := "sbt-velocity"

organization := "cchantep"

version := "0.2"

libraryDependencies += "org.apache.velocity" % "velocity-engine-core" % "2.4.1"

ThisBuild / Compile / javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

ThisBuild / Compile / doc / javacOptions --= Seq("-source", "1.8", "-target", "1.8")

// Scripted test
crossSbtVersions := Vector("0.13.11", "1.3.4")

enablePlugins(SbtPlugin)

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
