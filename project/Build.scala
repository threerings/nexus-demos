//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

import sbt.Keys._
import sbt._

object ReversiBuild extends Build {
  import spray.revolver.RevolverPlugin.Revolver

  val builder = new samskivert.ProjectBuilder("pom.xml") {
    override val globalSettings = Seq(
      crossPaths    := false,
      javacOptions  ++= Seq("-Xlint", "-Xlint:-serial", "-source", "1.6", "-target", "1.6"),
      javaOptions   ++= Seq("-ea"),
      // "-Xdebug", "-Xrunjdwp:transport=dt_socket,address=127.0.0.1:8888,server=y,suspend=y"),
      fork in Compile := true,
      autoScalaLibrary in Compile := false // no scala-library dependency (except for tests)
    )
    override def projectSettings (name :String, pom :pomutil.POM) = name match {
      case "core" => seq(
        libraryDependencies ++= Seq(
          // scala test dependencies
          "com.novocode" % "junit-interface" % "0.7" % "test->default"
        )
      )
      case "java" => Revolver.settings ++ LWJGLPlugin.lwjglSettings
      case "server" => Revolver.settings // ++ seq()
      case _ => Nil
    }
  }

  lazy val assets = builder("assets")
  lazy val core   = builder("core")
  lazy val java   = builder("java")
  lazy val server = builder("server")

  // one giant fruit roll-up to bring them all together
  lazy val d11s = Project("reversi", file(".")) aggregate(assets, core, java, server)
}
