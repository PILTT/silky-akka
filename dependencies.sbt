resolvers ++= Seq(
  "Sonatype OSS Releases"            at "http://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots"           at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Public Repositories" at "https://oss.sonatype.org/content/groups/public/"
)

val jacksonVersion = "2.6.2"
val log4jVersion   = "2.4.1"
val slf4jVersion   = "1.7.12"
val akkaVersion    = "2.3.9"

val slf4j = Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "org.slf4j" % "slf4j-ext" % slf4jVersion
)

val log4j = Seq(
  "org.apache.logging.log4j" % "log4j-api"        % log4jVersion % "test",
  "org.apache.logging.log4j" % "log4j-core"       % log4jVersion % "test",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion % "test",
  "com.fasterxml.jackson.core"       % "jackson-databind"        % jacksonVersion % "test",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % jacksonVersion % "test"
)

val productionDependencies = slf4j ++ log4j ++ Seq(
  "com.github.piltt"  %% "silky"      % "1.0.39",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion
)

val testDependencies = Seq(
  "com.github.rhyskeepence" %% "clairvoyance-scalatest" % "1.0.108" % "test",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion  % "test",
  "org.scalatest"     %% "scalatest"    % "3.0.0-M10"  % "test"
)

libraryDependencies <++= scalaVersion { scala_version ⇒
  CrossVersion.partialVersion(scala_version) match {
    case Some((2, scalaMajor)) if scalaMajor >= 11 ⇒ Seq("org.scala-lang.modules" %% "scala-xml" % "1.0.3")
    case _ ⇒ Seq.empty
  }
}
libraryDependencies ++= productionDependencies ++ testDependencies
