package test

class FooSpec extends org.specs2.mutable.Specification {
  "Build version" should {
    "expose scalaVersion" in {
      val v = s"v${BuildVersion.scalaVersion}"

      v must_=== "v2.13.5"
    }
  }
}
