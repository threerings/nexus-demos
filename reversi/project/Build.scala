//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

import sbt.Keys._
import sbt._

object ReversiBuild extends samskivert.MavenBuild {
  import spray.revolver.RevolverPlugin.Revolver

  override val globalSettings = Seq(
    crossPaths    := false,
    javacOptions  ++= Seq("-Xlint", "-Xlint:-serial", "-source", "1.6", "-target", "1.6"),
    javaOptions   ++= Seq("-ea"),
    // "-Xdebug", "-Xrunjdwp:transport=dt_socket,address=127.0.0.1:8888,server=y,suspend=y"),
    fork in Compile := true,
    autoScalaLibrary in Compile := false, // no scala-library dependency (except for tests)
    libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test->default",
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")
  )

  override def moduleSettings (name :String, pom :pomutil.POM) = name match {
    case "java" => Revolver.settings
    case "server" => Revolver.settings ++ seq(
      fork in Test := true
    )
    case _ => Nil
  }

  override def profiles = Seq("java", "android", "html", "server")
}
