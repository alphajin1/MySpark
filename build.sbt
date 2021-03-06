name := "MySpark"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= {
  val sparkVer = "2.2.0"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVer,
    "org.apache.spark" %% "spark-mllib" % sparkVer,
    "org.apache.spark" %% "spark-graphx" % sparkVer,
    "org.apache.spark" %% "spark-sql" % sparkVer
  )
}