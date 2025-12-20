package engineering.spark.streaming.utility

import org.apache.spark.sql.types._

object OrderEventSchema {
  val schema: StructType = StructType(Seq(
    StructField("eventId", StringType, nullable = false),
    StructField("eventType", StringType, nullable = false),
    StructField("eventTimestamp", StringType, nullable = false),

    StructField("order", StructType(Seq(
      StructField("orderId", StringType, nullable = false),
      StructField("orderTimestamp", StringType, nullable = false),
      StructField("status", StringType, nullable = false),
      StructField("totalAmount", DoubleType, nullable = false),
      StructField("currency", StringType, nullable = false)
    ))),

    StructField("customer", StructType(Seq(
      StructField("customerId", StringType, nullable = false),
      StructField("customerName", StringType, nullable = false),
      StructField("email", StringType, nullable = false),
      StructField("country", StringType, nullable = false)
    ))),

    StructField("items", ArrayType(StructType(Seq(
      StructField("productId", StringType, nullable = false),
      StructField("productName", StringType, nullable = false),
      StructField("category", StringType, nullable = false),
      StructField("qty", IntegerType, nullable = false),
      StructField("unitPrice", DoubleType, nullable = false)
    )))),

    StructField("payment", StructType(Seq(
      StructField("paymentType", StringType, nullable = false),
      StructField("success", BooleanType, nullable = false)
    )))
  ))
}