organization := "cchantep"

name := "sbt-velocity-test-simple"

version := "0.2"

scalaVersion := "2.12.20"

crossScalaVersions := Seq(scalaVersion.value, "3.3.8")

libraryDependencies ++= {
  val specsVer = "4.21.0"

  Seq(
    "org.specs2" %% "specs2-core" % specsVer,
    "org.specs2" %% "specs2-junit" % specsVer)
}

// Velocity plugin
enablePlugins(VelocityPlugin)

velocityContext ~= { ctx =>
  ctx.put("appName", "simple-test")

  ctx
}
