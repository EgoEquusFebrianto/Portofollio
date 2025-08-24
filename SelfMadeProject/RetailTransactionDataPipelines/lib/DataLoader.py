from pyspark.sql import SparkSession
import os

def read_data(spark: SparkSession):   
    csv_path = "file:///home/kudadiri/ProjectHome/MiniProject/RetailTransactionDataPipelines/data/retail_transactions.csv"

    data = spark.read \
        .format("csv") \
        .option("inferSchema", "true") \
        .option("header", "true") \
        .load(csv_path)

    return data