from pyspark.sql import DataFrame, SparkSession
from pyspark.sql import functions as F

def data_cleaning_session(df: DataFrame):
    fill_value = {
        "Store": "Unknow Store",
        "Product": "Unknow Product"
    }

    product_category_map = {
        "Electronics": ["Laptop", "Phone", "TV", "Headphones"],
        "Clothing": ["Shirt", "Pants", "Shoes"],
        "Home": ["Sofa", "Table", "Chair"],
        "Sports": ["Ball", "Racket", "Shoes Sport"],
        "Beauty": ["Perfume", "Lotion", "Makeup"]
    }

    df_filled = df.na.fill(fill_value)
    df_drop_duplicates = df_filled.dropDuplicates(subset=["TransactionID"])
    df_clean = df_drop_duplicates \
        .withColumn("SalesBeforeDiscount", F.when(F.col("SalesBeforeDiscount") < 0, 0).otherwise(F.col("SalesBeforeDiscount"))) \
        .withColumn("Discount", F.when(F.col("Discount") > 100, 0).otherwise(F.col("Discount"))) \
        .withColumn(
            "Category",
            F.when(F.col("Category").isNull(),
                F.when(F.col("Product").isin(product_category_map["Electronics"]), F.lit("Electronics"))
                   .when(F.col("Product").isin(product_category_map["Clothing"]), F.lit("Clothing"))
                   .when(F.col("Product").isin(product_category_map["Home"]), F.lit("Home"))
                   .when(F.col("Product").isin(product_category_map["Sports"]), F.lit("Sports"))
                   .when(F.col("Product").isin(product_category_map["Beauty"]), F.lit("Beauty"))
            ).otherwise(F.col("Category"))
        )

    return df_clean

def df_add_column(spark: SparkSession, df: DataFrame):
    margin = [
        ("Electronics", 0.15),
        ("Clothing", 0.25),
        ("Home", 0.2),
        ("Sports", 0.18),
        ("Beauty", 0.3),
    ]

    table_margin = spark.createDataFrame(margin, ["Category", "Margin"])

    df_temp = df.join(table_margin, "Category", "left")
    df_adding_column = df_temp \
        .withColumn("Revenue", F.round(F.col("SalesBeforeDiscount") * (1 - F.col("Discount") / 100), 2)) \
        .withColumn("CategoryDiscount",
            F.when(F.col("Discount") < 2, "Low")
             .when(F.col("Discount") < 5, "Medium")
             .otherwise("High")
        ) \
        .withColumn("ProfitEstimate", F.round(F.col("Revenue") * F.col("Margin"), 2))

    return df_adding_column

def agg_sales_per_store_and_category(df: DataFrame):
    res = df.groupby("Store", "Category") \
        .agg(
            F.round(F.sum("Revenue"), 2).alias("TotalRevenue"),
            F.round(F.avg("Discount"), 2).alias("AverageDiscount")
        ) \
        .orderBy(F.col("TotalRevenue").desc())

    return res

def agg_daily_summary(df: DataFrame):
    res = df.groupby("Category", "TransactionDate") \
        .agg(
            F.round(F.sum("Revenue"), 2).alias("TotalRevenue"),
            F.round(F.avg("Discount"), 2).alias("AverageDiscount"),
            F.round(F.sum("ProfitEstimate"), 2).alias("ProfitEstimate"),
        ) \
        .orderBy(F.col("TransactionDate"))

    return res

def agg_monthly_summary(df: DataFrame):
    res = df \
        .withColumn("Month", F.date_format(F.col("TransactionDate"), "y-M")) \
        .groupby("Month") \
        .agg(
            F.round(F.sum("Revenue"), 2).alias("TotalRevenue"),
            F.round(F.avg("Discount"), 2).alias("AverageDiscount"),
            F.round(F.sum("ProfitEstimate"), 2).alias("TotalProfitEstimate")
        ) \
        .orderBy(F.col("Month"))

    return res
