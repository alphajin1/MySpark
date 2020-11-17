package examples.graphx.simrank

import common.MySparkSession
import org.apache.log4j.Logger
import org.apache.spark.rdd.RDD

object SimRankppEx01 {
  def main(args: Array[String]): Unit = {
    val logger = Logger.getRootLogger
    val (spark, sc) = MySparkSession.getDefault(s"${this.getClass.getSimpleName}")

    val df = spark.read.csv("data/simrank/relqueries_sample.csv")
    val rawEdges: RDD[(String, String, Double)] = df.rdd.map(x => (x.getString(0), x.getString(1), x.getString(3).toDouble))
//
//    val df = spark.read.csv("data/simrank/simrank_pp_fig3.csv")
//    val rawEdges: RDD[(String, String, Double)] = df.rdd.map(x => (x.getString(0), x.getString(1), x.getString(2).toDouble))

    // SimRank [2] 논문의 경우 *UnDirectedGraph* 이다.
    val graph = SimRankpp.getUnDirectedGraphFromRawEdges(rawEdges)
    val normalizedEdges = SimRankpp.getSpreadNormalizedEdges(graph)
    val matrixes = SimRankpp.getResultMatrix(graph, normalizedEdges, 0.8, 1)

    logger.warn("[START] resultMatrix")
    val result1DF = SimRankpp.getResultDataFrame(spark, matrixes._1, graph)
    import spark.implicits._
    result1DF.filter($"query" === "스타벅스").orderBy($"weight".desc).show(100, false)

    val result2DF = SimRankpp.getResultDataFrame(spark, matrixes._2, graph)
    result2DF.filter($"query" === "스타벅스").orderBy($"weight".desc).show(100, false)

    val result3DF = SimRankpp.getResultDataFrame(spark, matrixes._3, graph)
    result3DF.filter($"query" === "스타벅스").orderBy($"weight".desc).show(100, false)
//    resultDF.filter($"query" === "스타벅스").orderBy($"weight".asc).show(100, false)
//    resultDF.show(200, false)
  }
}
