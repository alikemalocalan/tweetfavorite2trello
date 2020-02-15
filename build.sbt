/*
  General Scala attributes
 */
scalaVersion := "2.13.1"

/*
  General project attributes
 */
name := "OrganizeTweetApp"
version := "0.1"

val http4sVersion = "0.20.10"

// Only necessary for SNAPSHOT releases
resolvers += Resolver.sonatypeRepo("snapshots")

/*
  Project dependencies
 */
libraryDependencies ++= Seq(
  "org.springframework.boot" % "spring-boot-starter-web" % "2.1.8.RELEASE",
  "org.springframework.boot" % "spring-boot-configuration-processor" % "2.1.7.RELEASE",
  "org.json4s" %% "json4s-native" % "3.6.7",
  "org.json4s" %% "json4s-jackson" % "3.6.7",
  "org.twitter4j" % "twitter4j-core" % "4.0.7",
  "com.typesafe" % "config" % "1.3.4"
)

/*
  Packaging plugin
 */

// enable the Java app packaging archetype and Ash script (for Alpine Linux, doesn't have Bash)
enablePlugins(JavaAppPackaging, AshScriptPlugin)

// set the main entrypoint to the application that is used in startup scripts
mainClass in Compile := Some("com.github.alikemalocalan.OrganizeTweetApp")

// the Docker image to base on (alpine is smaller than the debian based one (120 vs 650 MB)
dockerBaseImage := "openjdk:8-jre-alpine"

// creates tag 'latest' as well when publishing
dockerUpdateLatest := true