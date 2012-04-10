import sbtrelease.Release._

import OsgiKeys._

name := "Model Hibernate JPA"

organization := "com.justcloud"

crossPaths := false

autoScalaLibrary := false

seq(releaseSettings: _*)

seq(osgiSettings: _*)

exportPackage := Seq(
    "com.justcloud.module"
)

libraryDependencies ++= Seq(
    //Unit and acceptance tests
    //(test) -> Specs2 
    "org.specs2"         %% "specs2"                      % "1.8.2"     % "test",
    //
    //Integration Tests
    //  Junit
    "junit"              % "junit"                        % "4.9"       % "it",
    "com.novocode"       % "junit-interface"              % "0.8"       % "it",
    //(it)->  Felix
    "org.apache.felix"   % "org.apache.felix.framework"   % "4.0.2"     % "it",
    //(it)->  Logback
    "ch.qos.logback"     % "logback-core"                 % "1.0.1"     % "it",
    "ch.qos.logback"     % "logback-classic"              % "1.0.1"     % "it",
    //(it)->  Javax Inject
    "javax.inject"       % "javax.inject"                 % "1"         % "it",
    //(it)->  Pax Exam
    "org.ops4j.pax.exam" % "pax-exam-junit4"              % "2.4.0.RC1" % "it",
    "org.ops4j.pax.exam" % "pax-exam-container-native"    % "2.4.0.RC1" % "it",
    "org.ops4j.pax.exam" % "pax-exam-link-mvn"            % "2.4.0.RC1" % "it",
    //(it)->  Pax Url
    "org.ops4j.pax.url"  % "pax-url-link"                 % "1.4.0.RC1" % "it",
    "org.ops4j.pax.url"  % "pax-url-aether"               % "1.4.0.RC1" % "it"
)

publishTo <<= (version) { version: String =>
      Some(Resolver.file("file", new File("/home/iamedu/Repos/justcloud/repository") / {
        if  (version.trim.endsWith("SNAPSHOT"))  "snapshots"
        else                                     "releases/" }    ))
}

