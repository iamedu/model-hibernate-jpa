import sbtrelease.Release._

import OsgiKeys._

name := "model-hibernate-jpa"

organization := "com.justcloud"

crossPaths := false

autoScalaLibrary := false

seq(releaseSettings: _*)

seq(osgiSettings: _*)

exportPackage := Seq(
    "com.justcloud.model.hibernate.jpa;version=\"0.1.0\""
)

importPackage := Seq(
    "com.justcloud.model.dao;version=\"0.1.0\"",
    "com.justcloud.model.domain;version=\"0.1.0\""
)

libraryDependencies ++= Seq(
    //Compile dependencies (REMEMBER THERE MUST BE OSGI MODULES AVAILABLE)
    "com.justcloud" % "model-api"               % "0.1.0" withSources() withJavadoc(),
    //Hibernate
    "org.hibernate" % "hibernate-core"          % "4.1.0.Final" withSources(),
    "org.hibernate" % "hibernate-ehcache"       % "4.1.0.Final" withSources(),
    "org.hibernate" % "hibernate-entitymanager" % "4.1.0.Final" withSources(),
    //
    //Unit and acceptance tests
    //(test) -> Specs2 
    "org.specs2"         %% "specs2"                      % "1.8.2"     % "test",
    //
    //Integration Tests
    //  Junit
    "junit"                  % "junit"                      % "4.9"       % "it",
    "com.novocode"           % "junit-interface"            % "0.8"       % "it",
    //(it)->  Felix
    "org.apache.felix"       % "org.apache.felix.framework" % "4.0.2"     % "it",
    "org.apache.felix"       % "org.osgi.service.obr"       % "1.0.2"     % "it",
    //(it)->  Logback
    "ch.qos.logback"         % "logback-core"               % "1.0.1"     % "it",
    "ch.qos.logback"         % "logback-classic"            % "1.0.1"     % "it",
    //(it)->  Javax Inject
    "javax.inject"           % "javax.inject"               % "1"         % "it",
    //(it)->  Pax Exam
    "org.ops4j.pax.exam"     % "pax-exam-junit4"            % "2.4.0.RC1" % "it",
    "org.ops4j.pax.exam"     % "pax-exam-container-native"  % "2.4.0.RC1" % "it",
    "org.ops4j.pax.exam"     % "pax-exam-link-mvn"          % "2.4.0.RC1" % "it",
    //(it)->  Pax Url
    "org.ops4j.pax.url"      % "pax-url-link"               % "1.4.0.RC1" % "it",
    "org.ops4j.pax.url"      % "pax-url-aether"             % "1.4.0.RC1" % "it"
)

compileOrder := CompileOrder.JavaThenScala

publishTo <<= (version) { version: String =>
      Some(Resolver.file("file", new File("/home/iamedu/Repos/justcloud/repository") / {
        if  (version.trim.endsWith("SNAPSHOT"))  "snapshots"
        else                                     "releases/" }    ))
}

resolvers ++= Seq(
    "JustCloud Releases" at "https://raw.github.com/just-cloud/maven/master/releases/"
)

