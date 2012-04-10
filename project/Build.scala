import sbt._
import Keys._

object ModelHibernateJPAProject extends Build {
  lazy val root =
    Project("Model Hibernate JPA", file("."))
      .configs( IntegrationTest )
      .settings( Defaults.itSettings : _*)
}
