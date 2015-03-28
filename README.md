# Intro to Spark

Into to exploring data and extracting useful information using [Apache Spark)(http://spark.apache.org).
The code was presented at the Scala User Group Cologne, slides are [here](http://www.slideshare.net/marius-soutier/spark-intro-scala-ug).

There are several jobs provided that you can run for each topic. Build the JAR using `sbt ";clean;assembly"`.
Run it on Spark using `spark-submit --master <master> --class <f.q.ClassName> target/scala-2.10/spark-demo-assembly-1.0.jar`.

For interactive exploration, you can use `spark-shell`. When you run it, it already provides a SparkContext called `sc`.

If you are using an IDE like IntelliJ or Eclipse, you should try out their worksheets or Scala console.
When you are starting a SparkContext, pass the master explicitly, e.g.:
`val sc = new SparkContext(master = "<master>", appName = "Demo")`.

*Hint*: You can easily start any job or shell by using the Spark local mode, e.g. `spark-shell --master local[*]`.

## Google Web Graph

* Download `web-Google.txt.gz` from https://snap.stanford.edu/data/web-Google.html and put it in src/main/resources.
* Check out the `GoogleWebGraph.scala` job.


## GitHub Commits

1. Obtain an OAuth key on GitHub (Settings > Applications > Personal access tokens > Generate)
2. Execute `download_github.sh`
3. Execute `ProcessGitHubData.scala`
-> the files should be in `src/main/resources/github`, one commit per line

Now play around with `GitHubSql.scala`.


## Audioscrobbler

* Download Audioscrobbler profile data from http://www-etud.iro.umontreal.ca/~bergstrj/audioscrobbler_data.html and put it in src/main/resources.
* Check out the `Audioscrobbler.scala` job.
