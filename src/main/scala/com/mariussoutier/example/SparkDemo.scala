package com.mariussoutier.example

trait SparkDemo {

  // Naive regex to split words based on whitespace
  val wordRegEx = "\\s+"

  // Google Node Data

  val googleData = "src/main/resources/web-Google.txt.gz"

  // GitHub commits

  val commits = "src/main/resources/github/"

  // Audio Scrobbler Data

  val audioScrobbler = "src/main/resources/audioscrobbler"

}

object SparkDemo extends SparkDemo
