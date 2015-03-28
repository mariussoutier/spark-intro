package com.mariussoutier.example

import org.apache.spark.SparkContext
import scala.util.Try

object Audioscrobbler extends App {

  // Pass master and appName when running from a console/shell/worksheet.
  val sc = new SparkContext()


  import SparkDemo.audioScrobbler
  val userArtistData = sc.textFile(s"$audioScrobbler/user_artist_data.txt")
  val artistData = sc.textFile(s"$audioScrobbler/artist_data.txt")

  /*
   After loading the data, experiment with it. Check out file lengths using .count(), look at the data using take().
   From the README.txt you know that artist_data.txt has two columns, artistId artistName, separated by tab.
   */

  //...

  /*
  As an example, we want to find the top 10 played artist, but with their name instead of ID.
   */

  val artists = artistData.filter(!_.isEmpty).flatMap { line =>
    // This dataset hasn't been cleaned so we just keep those entries that match our RegEx and ignore the rest
    Try {
      val Array(artistId, name) = line.split("\t", 2)
      artistId.toInt -> name
    }.toOption
  }
  .cache()

  // Parse and key by artistId
  val playsByArtist = userArtistData.filter(!_.isEmpty).map { line =>
    val Array(userId, artistId, playCount) = line.split("\\s+", 3)
    artistId.toInt -> (userId.toInt, playCount.toLong)
  }
  .cache()

  import SparkContext._
  val artistPlays = playsByArtist.join(artists).map { case (artistId, ((userId, playCount), artistName)) => artistName -> playCount }

  val topTen = artistPlays.
    reduceByKey(_ + _).
    sortBy({ case (_, playCount) => playCount }, ascending = false).
    take(10)

  // Task: find users with similar taste, e.g. playcount > 2 for at least three common artists.

  sc.stop()
}
