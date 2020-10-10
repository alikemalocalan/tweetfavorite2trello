/*
  General Scala attributes
 */
scalaVersion := "2.13.1"

/*
  General project attributes
 */
name := "OrganizeTweetApp"
version := "1.0"

val akkaVersion = "2.5.31"
val akkaHttpVersion = "10.2.1"

// Only necessary for SNAPSHOT releases
resolvers += Resolver.sonatypeRepo("releases")
resolvers += "jitpack" at "https://jitpack.io"
/*
  Project dependencies
 */
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe" % "config" % "1.4.0",
  "org.twitter4j" % "twitter4j-core" % "4.0.7",
  "com.taskadapter" % "trello-java-wrapper" % "0.14",
  "com.github.Steppschuh" % "Java-Markdown-Generator" % "1.3.2",
  "org.scalatest" %% "scalatest" % "3.2.2" % Test
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