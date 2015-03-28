import _root_.sbtassembly.Plugin.AssemblyKeys._
import _root_.sbtassembly.Plugin.MergeStrategy
import _root_.sbtassembly.Plugin.PathList
import _root_.sbtassembly.Plugin._

name := "spark-intro"

organization := "com.mariussoutier.example"
version := "1.0"

scalaVersion := "2.10.5"

val sparkVersion = "1.2.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "com.typesafe.play" %% "play-json" % "2.3.8"
)

assemblySettings

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) => {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.startsWith("META-INF") => MergeStrategy.discard
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.first
  case PathList("org", "apache", xs@_*) => MergeStrategy.first
  case PathList("org", "jboss", xs@_*) => MergeStrategy.first
  case "about.html" => MergeStrategy.rename
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}}
