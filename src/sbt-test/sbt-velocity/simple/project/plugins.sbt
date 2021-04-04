lazy val pluginVer = sys.props("sbt-velocity.version")

addSbtPlugin("cchantep" % "sbt-velocity" % pluginVer changing())
