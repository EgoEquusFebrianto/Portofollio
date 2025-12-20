package engineering.spark.streaming.lib

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SparkSession}

object StreamProcessingUtility {
  def readStreamJob(spark: SparkSession, servers: String, topic: String): DataFrame = {
      spark.readStream
        .format("kafka")
        .option("kafka.bootstrap.servers", servers)
        .option("subscribe", topic)
        .option("startingOffsets", "earliest")
        .load()
  }

  def streamingTransformation(data: DataFrame, schema: StructType): DataFrame = {
    val jsonDf = data
      .select(col("value").cast(StringType).as("json_value"))

    val parseDf = jsonDf
      .select(
        from_json(
          col("json_value"),
          schema
        ).alias("data")
      )
      .select("data.*")

    val enrichedDf = parseDf
      .withColumn("eventTimestamp", to_timestamp(col("eventTimestamp")))
      .withColumn(
        "order",
        col("order").withField(
          "orderTimestamp",
          to_timestamp(col("order.orderTimestamp"))
        )
      )

    enrichedDf
  }
}
