# SBT Velocity plugin

SBT source and resource generators using templates with [Apache Velocity](https://velocity.apache.org/).

## Motivation

It's easy to [generate files with SBT](https://www.scala-sbt.org/1.x/docs/Howto-Generating-Files.html), but doing so with a template can be even easier.

**Examples:**

- [Simple](src/sbt-test/sbt-velocity/simple/)

## Get started

This plugin requires SBT 0.13+.

You need to update the `project/plugins.sbt`.

```scala
resolvers ++= Seq(
  "Tatami Releases" at "https://raw.github.com/cchantep/tatami/master/releases")

addSbtPlugin("cchantep" % "sbt-velocity" % "0.1")
```

The plugin is not enable automatically, it can be done per-project in the build.

```scala
enablePlugins(VelocityPlugin)
```

The following settings are available.

**`velocityContext`** - The [velocity context](https://velocity.apache.org/engine/2.3/apidocs/org/apache/velocity/VelocityContext.html) to pass values to the templates.
By default, the following values are set:

- `scalaVersion`
- `scalaBinaryVersion`

This context can be updated:

```scala
velocityContext ~= { ctx =>
  ctx.put("appName", "simple-test")

  ctx
}
```

**`velocityFileNaming`** - The strategy to name the generated files.
By default, strip the `.vm` prefix from the template name (e.g. `template.scala.vm` => `template.scala`).

**`velocityEngineProperties`** - The engine properties (by default: empty)

**`(Compile|Test) / velocityGeneration / sourceDirectory`** - The directory where the source templates are expected to be (by default: `(Compile|Test) / sourceDirectory / "vtmpl"`).

**`(Compile|Test) / velocityGeneration / sourceManaged`** - The directory where the sources are generated (by default: `(Compile|Test) / sourceManaged / "velocity"`).

**`(Compile|Test) / velocityGeneration / resourceDirectory`** - The directory where the resource templates are expected to be (by default: `(Compile|Test) / resourceDirectory / "vtmpl"`).

**`(Compile|Test) / velocityGeneration / resourceManaged`** - The directory where the resources are generated (by default: `(Compile|Test) / resourceManaged / "velocity"`).

> The templates in the `sourceDirectory` or `resourceDirectory` must have names ending with `.vm` extension.

## Build

This is built using SBT.

    sbt '^ publishLocal'

[![Scala CI](https://github.com/cchantep/sbt-velocity/actions/workflows/scala.yml/badge.svg)](https://github.com/cchantep/sbt-velocity/actions/workflows/scala.yml)
