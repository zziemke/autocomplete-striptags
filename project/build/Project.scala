import sbt._

class Project(info: ProjectInfo) extends DefaultWebProject(info) {
	
	override def jettyWebappPath  = webappPath
	override def scanDirectories = mainCompilePath :: testCompilePath :: Nil

  val mavenLocal = "Local Maven Repository" at
  "file://"+Path.userHome+"/.m2/repository"

  val scalatools_snapshot = "Scala Tools Snapshot" at
  "http://scala-tools.org/repo-snapshots/"

  val scalatools_release = "Scala Tools Release" at
  "http://scala-tools.org/repo-releases/"
  
  val liftVersion = "2.0-RC1"

  override def libraryDependencies = Set(
    "net.liftweb" % "lift-mapper" % liftVersion % "compile->default",
     "net.liftweb" % "lift-widgets"  % liftVersion % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.24" % "test->default",
		"org.scalatest" % "scalatest" % "1.0.1" % "test->default"
    ) ++ super.libraryDependencies
}