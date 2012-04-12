package com.justcloud

import org.junit.Assert.assertNotNull
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import org.ops4j.pax.exam.junit.Configuration
import org.ops4j.pax.exam.junit.JUnit4TestRunner
import org.osgi.framework.BundleContext
import javax.inject.Inject
import org.osgi.framework.Bundle
import com.justcloud.test.model.PersonDao
import com.justcloud.test.model.PersonDaoImpl
import javax.naming.InitialContext

@RunWith(classOf[JUnit4TestRunner])
class BundleIntegrationTest extends OsgiHelper {

  @Inject
  private var context: BundleContext = _

  private var thisBundle: Bundle = _

  private var dataBundle: Bundle = _

  @Test
  def datasourceIsPublished(): Unit = {
    assert(thisBundle.getState() == 32)
    
    //assertNotNull(waitForService(dataBundle, Class.forName("javax.sql.XADataSource")))
    assertNotNull(waitForService(thisBundle, classOf[PersonDao]))
  }

  @Before
  def before(): Unit = {
    //Loading our bundle for the model api
    deploy(context, "com.justcloud.model.api",
      "com.springsource.org.hibernate.ejb",
      "org.hibernate.validator",
      "derby")

    //Load data sources
    dataBundle = findBundle(context, "com.justcloud.model.hibernate.jpa.data")
    dataBundle.update()
    dataBundle.start()

    //This bundle can now resolve
    thisBundle = findBundle(context, "com.justcloud.model.hibernate.jpa.tests")
    thisBundle.update()
    thisBundle.start()

    //Starting everything else
    deploy(context,
      "org.apache.aries.transaction.blueprint",
      "org.apache.aries.jpa.blueprint.aries",
      "org.apache.aries.blueprint",
      "org.apache.aries.jpa.container",
      "org.apache.aries.jpa.container.context",
      "org.apache.aries.jndi")

  }

  @Configuration
  def options() = osgiOptions

}
