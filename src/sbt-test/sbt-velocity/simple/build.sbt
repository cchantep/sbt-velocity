organization := "cchantep"

name := "sbt-velocity-test-simple"

version := "0.1"

scalaVersion := "2.13.5"

crossScalaVersions := Seq("2.12.13", scalaVersion.value)

libraryDependencies ++= {
  val specsVer = "4.10.6"

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
