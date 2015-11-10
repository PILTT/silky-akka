resolvers     += Resolver.sonatypeRepo("releases")
updateOptions := updateOptions.value.withCachedResolution(cachedResoluton = true)

val log4jVersion = "2.4.1"
val slf4jVersion = "1.7.12"
val akkaVersion  = "[2.3.5,2.4.99]"

val slf4j = Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "org.slf4j" % "slf4j-ext" % slf4jVersion
)

val log4j = Seq(
  "org.apache.logging.log4j" % "log4j-api"        % log4jVersion % "test",
  "org.apache.logging.log4j" % "log4j-core"       % log4jVersion % "test",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion % "test",
  "com.fasterxml.jackson.core"       % "jackson-databind"        % "2.6.3" % "test",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.6.2" % "test" exclude("org.yaml", "snakeyaml")
)

val productionDependencies = slf4j ++ log4j ++ Seq(
  "com.github.piltt"  %% "silky"      % "1.0.42",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion % "provided"
)

val testDependencies = Seq(
  "com.github.rhyskeepence" %% "clairvoyance-scalatest" % "1.0.108" % "test",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion  % "test",
  "org.scalatest"     %% "scalatest"    % "3.0.0-M11"  % "test"
)

libraryDependencies <++= scalaVersion { scala_version ⇒
  CrossVersion.partialVersion(scala_version) match {
    case Some((2, scalaMajor)) if scalaMajor >= 11 ⇒ Seq("org.scala-lang.modules" %% "scala-xml" % "1.0.5")
    case _ ⇒ Seq.empty
  }
}
libraryDependencies ++= productionDependencies ++ testDependencies
