package com.mariussoutier.example

import org.apache.spark.SparkContext

/**
 * Explore Google web graph data.
 */
object GoogleWebGraph extends App {

  // Pass master and appName when running from a console/shell/worksheet.
  val sc = new SparkContext()


  val rawData = sc.textFile(SparkDemo.googleData)

  // Let's peek at the data
  rawData.first()
  // Let's see more data to jump to actual data instead of just comments.
  rawData.take(5).foreach(println)

  // As we can see, there are two columns with a fromNode -> toNode relationship (a node is just a web url).

  // Cache the data
  rawData.cache()
  // Take a look at the WebUI, by default on http://localhost:4040.
  rawData.take(5).foreach(println)
  // Now look again, at the Storage tab.

  // Let's filter all comment lines. We can't just drop the first lines because order cannot always be guaranteed.
  val data = rawData.filter(!_.startsWith("#"))

  // Parse the lines into link relationships and de-dupe.
  val links = data.map { line =>
    val Array(fromNode, toNode) = line.split("\\s+")
    fromNode.toInt -> toNode.toInt
  }
  .distinct()
  .cache()

  // We need this import for PairRDDs (key -> value RDDs).
  import SparkContext._

  // count number of distinct nodes
  links.countByKey()

  // Invert the index, count how many links each node receives
  val naivePageRank = links
    .map { case (from, to) => to -> 1 }
    .reduceByKey(_ + _)

  val top10Pages = naivePageRank
    .sortBy ({ case (_, count) => count }, ascending = false)
    .take(10)

  // TASK: compute a real page rank, http://en.wikipedia.org/wiki/PageRank (code examples),
  // or http://pr.efactory.de/e-pagerank-algorithm.shtml.

  // Stop the context. Stopping the app will have the same effect.
  sc.stop()
}
