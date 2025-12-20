package engineering.spark.streaming.lib

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSessionBuilder {
  def build(config: SparkConf): SparkSession = {
    SparkSession.builder()
      .config(config)
      .getOrCreate()
  }
}