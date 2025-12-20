package engineering.spark.streaming

import org.slf4j.{Logger, LoggerFactory}
import engineering.spark.streaming.lib.{StreamProcessingUtility, SparkSessionBuilder}
import engineering.spark.streaming.utility.{OrderEventSchema, SparkConfiguration}

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object SparkProcessingApp {
  @transient private lazy val logger: Logger = LoggerFactory.getLogger("SparkProcessingApp")

  def main(args: Array[String]): Unit = {

    val servers = "172.25.5.7:9092"
    val topic = "testing"

    logger.info("Program Running..")
    val config = SparkConfiguration.getSparkConfig("local_env")
    val spark = SparkSessionBuilder.build(config)

    val kafkaDf = StreamProcessingUtility.readStreamJob(spark, servers, topic)
    val transformDf = StreamProcessingUtility.streamingTransformation(kafkaDf, OrderEventSchema.schema)

    transformDf.printSchema()
    transformDf.writeStream
      .format("console")
      .option("truncate", false)
      .option("numRows", 5)
      .start()
      .awaitTermination()

    logger.info("Test Berhasil")
    spark.close()
  }
}