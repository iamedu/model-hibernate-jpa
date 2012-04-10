package com.justcloud

import org.junit.runner.RunWith
import org.ops4j.pax.exam.junit.JUnit4TestRunner
import org.ops4j.pax.exam.CoreOptions._
import org.ops4j.pax.exam.junit.Configuration
import org.junit.Test
import org.junit.Assert._
import org.osgi.framework.BundleContext
import org.slf4j.LoggerFactory
import javax.inject.Inject

@RunWith(classOf[JUnit4TestRunner])
class BundleIntegrationTest {

  @Inject
  private var context: BundleContext = _

  @Configuration
  def opts() = options(
    repositories(
      "http://repo1.maven.org/maven2",
      "http://repository.springsource.com/maven/bundles/external",
      "https://oss.sonatype.org/content/groups/scala-tools/"
    ),
    mavenBundle("com.weiglewilczek.scala-lang-osgi", "scala-library", "2.9.1"),
    junitBundles(),
    felix(),
    equinox(),
    knopflerfish()
  )

  @Test
  def testContextIsNotNull() = {
    assertNotNull(context)
  }

}

