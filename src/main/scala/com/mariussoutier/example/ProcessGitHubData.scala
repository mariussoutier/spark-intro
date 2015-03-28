package com.mariussoutier.example

import java.io.File

import org.apache.commons.io.FileUtils
import play.api.libs.json.{JsValue, Json}

import scala.io.Source

/**
 * 1. Download files using download_github.sh
 * 2. Run this
 */
object ProcessGitHubData extends App {
  for {
    i <- 1 to 102
  } {
    val content = Source.fromFile(s"/tmp/github/commits$i.json").getLines().mkString
    val commits = Json.parse(content).as[Seq[JsValue]]
    FileUtils.writeStringToFile(new File(s"src/main/github/commits$i.json"), commits.map(_.toString()).mkString("\n") + "\n")
  }
}
