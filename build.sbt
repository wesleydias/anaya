name := "anaya"

version := "1.0.0"

organization := "wesleydias"

scalaVersion := "2.10.5"

crossScalaVersions := Seq("2.10.5")

sparkVersion := "1.5.2"

val testSparkVersion = settingKey[String]("The version of Spark to test against.")

testSparkVersion := sys.props.get("spark.testVersion").getOrElse(sparkVersion.value)

val testHadoopVersion = settingKey[String]("The version of Hadoop to test against.")

testHadoopVersion := sys.props.getOrElse("hadoop.testVersion", "2.2.0")

sparkComponents := Seq("core", "sql")

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "org.slf4j" % "slf4j-api" % "1.7.5" % "provided",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-client" % testHadoopVersion.value % "test",
  "org.apache.spark" %% "spark-core" % testSparkVersion.value % "test" exclude("org.apache.hadoop", "hadoop-client"),
  "org.apache.spark" %% "spark-sql" % testSparkVersion.value % "test" exclude("org.apache.hadoop", "hadoop-client")
)

// This is necessary because of how we explicitly specify Spark dependencies
// for tests rather than using the sbt-spark-package plugin to provide them.
spIgnoreProvided := true

publishMavenStyle := true

spAppendScalaVersion := true

spIncludeMaven := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <url>https://github.com/wesleydias/anaya</url>
  <licenses>
    <license>
      <name>Apache License, Verision 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:wesleydias/anaya.git</url>
    <connection>scm:git:git@github.com:wesleydias/anaya.git</connection>
  </scm>
  <developers>
    <developer>
      <id>wesleydias</id>
      <name>Wesley Dias</name>
      <url>https://in.linkedin.com/in/wesley-dias-43825a8</url>
    </developer>
  </developers>)

spName := "wesleydias/anaya"

parallelExecution in Test := false

ScoverageSbtPlugin.ScoverageKeys.coverageHighlighting := {
  if (scalaBinaryVersion.value == "2.10") false
  else true
}

credentials += Credentials(Path.userHome / ".ivy2" / ".sbtcredentials")

licenses += "Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0")
