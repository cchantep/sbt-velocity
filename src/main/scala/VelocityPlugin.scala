package cchantep

import scala.util.control.NonFatal

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin
import sbt.librarymanagement.Configuration

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine

object VelocityPlugin extends AutoPlugin {
  override def trigger = noTrigger
  override def requires = JvmPlugin

  object autoImport {
    val velocityGeneration = settingKey[Boolean](
      "Source generation using velocity engine")

    val velocityEngineProperties = settingKey[Map[String, String]](
      "Velocity engine properties")

    val velocityContext = settingKey[VelocityContext]("Velocity context")

    val velocityFileNaming = settingKey[String => String](
      "Velocity file naming")
  }

  import autoImport._

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    velocityGeneration := true,
    velocityContext := {
      val ctx = new VelocityContext()

      ctx.put("scalaVersion", scalaVersion.value)
      ctx.put("scalaBinaryVersion", scalaBinaryVersion.value)

      ctx
    },
    velocityFileNaming := { (n: String) => n.stripSuffix(".vm") },
    velocityEngineProperties := Map.empty[String, String]
  ) ++ sourceGenerator(Compile) ++ sourceGenerator(
    Test) ++ resourceGenerator(Compile) ++ resourceGenerator(Test)

  // ---

  private def sourceGenerator(config: Configuration): Seq[Def.Setting[_]] = {
    val scope = config / velocityGeneration

    Seq(
      scope / sourceDirectory := (config / sourceDirectory).value / "vtmpl",
      scope / sourceManaged := (config / sourceManaged).value / "velocity",
      config / sourceGenerators += Def.task[Seq[File]] {
        val props = velocityEngineProperties.value + (
          "file.resource.loader.path" -> (
            scope / sourceDirectory).value.getAbsolutePath
        )

      generate(
        logger = streams.value.log,
        engineProperties = props,
        templateDir = (scope / sourceDirectory).value,
        context = velocityContext.value,
        targetDir = (scope / sourceManaged).value,
        fileNaming = velocityFileNaming.value)
    }.taskValue)
  }

  private def resourceGenerator(config: Configuration): Seq[Def.Setting[_]] = {
    val scope = config / velocityGeneration

    Seq(
      scope / resourceDirectory := (config / resourceDirectory).value / "vtmpl",
      scope / resourceManaged := (resourceManaged).value / "velocity",
      config / resourceGenerators += Def.task[Seq[File]] {
        val props = velocityEngineProperties.value + (
          "file.resource.loader.path" -> (
            scope / resourceDirectory).value.getAbsolutePath
        )

        generate(
          logger = streams.value.log,
          engineProperties = props,
          templateDir = (scope / resourceDirectory).value,
          context = velocityContext.value,
          targetDir = (scope / resourceManaged).value,
          fileNaming = velocityFileNaming.value)
      }.taskValue
    )
  }

  private def generate(
    logger: util.Logger,
    engineProperties: Map[String, String],
    templateDir: File,
    context: VelocityContext,
    targetDir: File,
    fileNaming: String => String
  ): Seq[File] = {
    logger.info(s"Merging velocity templates from '$templateDir' ...")

    if (!targetDir.exists) {
      targetDir.mkdirs()
    }

    val eps = new java.util.Properties()

    engineProperties.foreach {
      case (k, v) => eps.put(k, v)
    }

    val engine = new VelocityEngine()

    engine.init(eps)

    io.IO.listFiles(templateDir, io.GlobFilter("*.vm")).flatMap { tf =>
      val name = tf.getName
      val tmpl = engine.getTemplate(name)
      val out = targetDir / fileNaming(name)
      val w = new java.io.FileWriter(out, false)

      try {
        tmpl.merge(context, w)

        logger.info(s"Merged template '${tf.getName}' to '$out'")

        Seq(out)
      } catch {
        case NonFatal(cause) =>
          logger.warn(s"Failed to merge template '${tf.getName}': $cause")

          Seq.empty[File]
      } finally {
        w.close()
      }
    }
  }
}
