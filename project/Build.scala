import sbt._
import Keys._
object Build {

  implicit def toWrapperProjectDefinition(project: Project): WrapperProject =
    new WrapperProject(project)

      class WrapperProject(val project: Project) {
    def inheritSettings(parent: Project): Project = project
  }
}
