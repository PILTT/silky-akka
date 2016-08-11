resolvers     += Resolver.sonatypeRepo("releases")
updateOptions := updateOptions.value.withCachedResolution(cachedResoluton = true)

val log4jVersion   = "[2.0.1,2.9.99]"
val slf4jVersion   = "[1.7.0,1.9.99]"
val jacksonVersion = "[2.6.2,2.7.99]"
val akkaVersion    = "[2.3.5,2.4.99]"

val slf4j = Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion % "provided",
  "org.slf4j" % "slf4j-ext" % slf4jVersion % "provided"
)

val log4j = Seq(
  "org.apache.logging.log4j" % "log4j-api"        % log4jVersion % "test",
  "org.apache.logging.log4j" % "log4j-core"       % log4jVersion % "test",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion % "test",
  "com.fasterxml.jackson.core"       % "jackson-databind"        % jacksonVersion % "test",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % jacksonVersion % "test" exclude("org.yaml", "snakeyaml")
)

val productionDependencies = slf4j ++ Seq(
  "com.github.piltt"  %% "silky"      % "[1.0.11,1.9.999]",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion % "provided"
)

val testDependencies = log4j ++ Seq(
  "com.github.rhyskeepence" %% "clairvoyance-scalatest" % "[1.0.109,1.0.999]" % "test",
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
