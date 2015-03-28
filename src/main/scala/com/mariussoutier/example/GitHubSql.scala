package com.mariussoutier.example

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.joda.time.DateTime

/**
 * Explore GitHub commits using SparkSQL.
 */
object GitHubSql extends App {
  // Pass master and appName when running from a console/shell/worksheet.
  val sc = new SparkContext()

  // Using SparkSQL means starting an SQLContext which in turn wraps the SparkContext
  val sqlContext = new SQLContext(sc)

  // SparkSQL can read JSON files directly...
  val commits = sqlContext.jsonFile(SparkDemo.commits)
  // ...and it also infers a schema - awesome!
  // If your data is very uniform, you can reduce the samplingRatio on jsonFile (default is 1.0).
  commits.printSchema()

  // commits is a SchemaRDD (DataFrame in 1.3) and allows to register a temporary in-memory table to run SQL commands.
  commits.registerTempTable("commits")

  // You can also save a permanent table to the Hive metastore. This is especially interesting when using other Hadoop
  // tools, or when accessing data using apps like Excel, Tableau, or Spark's SQL server.
  //commits.saveAsTable("commits")

  // Run SQL using the sql method. Sub-objects can be access via dot-notation.
  val authors = sqlContext.sql("SELECT DISTINCT commit.author.name as name FROM commits ORDER BY name")

  import sqlContext._
  // There is also a language-integrated query available, but it can be hard to write sometimes.
  val databricksCommitters = commits
    .select(Symbol("commit.author.name"))
    .where(Symbol("commit.author.email") like "%databricks%")
    .distinct()

  // The result of an SQL query is again a SchemaRDD, which is an in-memory table, i.e. named columns x rows.

  // One use case is to have a web app access data using SparkSQL. For that we need to collect the data back to the
  // driver and extract each value from the rows. The row is represent by sql.Row.

  val names = authors.map { row => row.getString(0) }.collect()

  // Just another example, let's extract the data using SQL and map to Scala case class.
  case class Commit(committer: String, email: String, date: DateTime)

  val sparkSqlCommits = sqlContext
    .sql("SELECT commit.author.name, commit.author.email, commit.author.date, WHERE commit.author.message LIKE '%[SQL]%'")
    // Now we are going back to RDD-land, RDD[Commit]
    .map { row => Commit(row.getString(0), row.getString(1), DateTime.parse(row.getString(2))) }

  // Then we save it as a Parquet file, which is a popular columnar file format on Hadoop and supports fast reads.
  // saveAsParquetFile is an implicit from the sqlContext._ import, available on all RDD[_ <: Product].
  sparkSqlCommits
    .saveAsParquetFile("/tmp/sql-committers.parquet")


  sc.stop()
}
