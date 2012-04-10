resolvers += Classpaths.typesafeResolver

resolvers += "gseitz@github" at "http://gseitz.github.com/maven/"

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.4")

addSbtPlugin("com.typesafe.sbtosgi" % "sbtosgi" % "0.1.0")

