package com.justcloud

import org.ops4j.pax.exam.CoreOptions._
import org.ops4j.pax.tinybundles.core.TinyBundles
import org.osgi.framework.Constants
import com.justcloud.model.domain.DomainObject
import com.justcloud.model.hibernate.jpa.GenericDaoJpa
import org.osgi.framework.BundleContext
import org.osgi.framework.Bundle
import org.osgi.util.tracker.ServiceTracker
import com.justcloud.test.model.Person
import com.justcloud.test.model.PersonDao
import com.justcloud.test.model.PersonDaoImpl
import org.osgi.service.obr.RepositoryAdmin
import java.util.Properties

trait OsgiHelper {

  def osgiOptions = {
    val props = new Properties()
    props.load(getClass().getResourceAsStream("/default.properties"))
    val packages = props.get("org.osgi.framework.system.packages").toString()
      .replaceFirst("\\$\\{jre-\\$\\{java.specification.version\\}\\}", props.get("jre-1.6").toString())
      .replaceFirst("javax.transaction.xa;version=\"0.0.0.1_006_JavaSE\", javax.transaction;version=\"0.0.0.1_006_JavaSE\",",
        "")
    options(
      frameworkProperty("org.osgi.framework.system.packages").value(packages),
      systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("DEBUG"),
      //Setup obr repository
      frameworkProperty("obr.repository.url").value("file:///home/iamedu/Repos/justcloud/obr/repository.xml"),
      repositories(
        "http://repo1.maven.org/maven2",
        "http://repository.springsource.com/maven/bundles/external",
        "https://oss.sonatype.org/content/groups/scala-tools/"),
      //Just to be able to write the test in scala
      mavenBundle("com.weiglewilczek.scala-lang-osgi", "scala-library", "2.9.1"),
      //The next bundles are meant to be able to use obr protocol
      mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-tracker", "1.5.0"),
      mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-property", "1.5.0"),
      mavenBundle("org.apache.felix", "org.osgi.service.obr", "1.0.2"),
      mavenBundle("org.apache.felix", "org.apache.felix.bundlerepository", "1.6.6"),
      mavenBundle("org.ops4j.pax.url", "pax-url-commons", "1.4.0.RC1"),
      mavenBundle("org.ops4j.pax.url", "pax-url-obr", "1.4.0.RC1"),
      //We go back to normal now
      junitBundles(),
      //Install our bundle
      provision(buildDataBundle(), buildThisBundle()))
  }

  def deploy(context: BundleContext, name: String*) = {
    val obr = waitForService(
      findBundle(context, "org.apache.felix.bundlerepository"),
      classOf[RepositoryAdmin])

    val names = List(name: _*)

    val resolver = obr.resolver()

    names.foreach(name => {
      val resources = obr.discoverResources("(symbolicname=" + name + ")")
      resolver.add(resources.last)
    })

    if (resolver.resolve()) {
      resolver.deploy(true)
    } else {
      val reqs = resolver.getUnsatisfiedRequirements()
      var str = ""
      reqs.foreach((req) => {
        str += req.getComment() + "\n"
      })
      throw new RuntimeException(str)
    }
  }

  def findBundle(context: BundleContext, name: String) = context.getBundles()
    .filter(_.getSymbolicName() == name)
    .head

  def waitForService[T](bundle: Bundle, clazz: Class[T]): T = {
    val context = bundle.getBundleContext()
    val serviceTracker = new ServiceTracker(context, clazz, null)
    try {
      serviceTracker.open()
      serviceTracker.waitForService(10 * 1000)
    } finally {
      serviceTracker.close()
    }
  }
  
  private def buildDataBundle() = TinyBundles.bundle()
    .add("OSGI-INF/blueprint/datasources.xml", getClass().getResourceAsStream("/datasources.xml"))
    .set(Constants.BUNDLE_SYMBOLICNAME, "com.justcloud.model.hibernate.jpa.data")
    .set(Constants.IMPORT_PACKAGE, "org.apache.derby.jdbc,javax.sql")
    .set(Constants.BUNDLE_VERSION, "1.0.0")
    .set(Constants.BUNDLE_MANIFESTVERSION, "2")
    .set("Bundle-Blueprint", "OSGI-INF/blueprint/datasources.xml")
    .build()

  private def buildThisBundle() = TinyBundles.bundle()
    .add(classOf[GenericDaoJpa[DomainObject]])
    .add(classOf[Person])
    .add(classOf[PersonDao])
    .add(classOf[PersonDaoImpl])
    .add("OSGI-INF/blueprint/daos.xml", getClass().getResourceAsStream("/daos.xml"))
    .add("META-INF/persistence.xml", getClass().getResourceAsStream("/persistence.xml"))
    .set(Constants.BUNDLE_SYMBOLICNAME, "com.justcloud.model.hibernate.jpa.tests")
    .set(Constants.EXPORT_PACKAGE, "com.justcloud.model.hibernate.jpa;version=\"0.1.0\"")
    .set(Constants.IMPORT_PACKAGE, "com.justcloud.model.dao;version=\"0.1.0\"," +
      "com.justcloud.model.domain;version=\"0.1.0\"," +
      "org.apache.derby.jdbc," +
      "javax.sql," +
      "org.hibernate.validator," +
      "javax.validation," +
      "javax.persistence;version=\"[2.0,3.0)\"," +
      "org.hibernate.ejb;version=\"[4.1.0,5)\"," +
      "org.hibernate.proxy;version=\"[4.1.0,5)\"")
    .set(Constants.BUNDLE_VERSION, "1.0.0")
    .set(Constants.BUNDLE_MANIFESTVERSION, "2")
    .set("Bundle-Blueprint", "OSGI-INF/blueprint/daos.xml")
    .set("Meta-Persistence", "META-INF/persistence.xml")
    .build()

}