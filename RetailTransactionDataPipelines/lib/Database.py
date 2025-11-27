from pyspark.sql import DataFrame

def data_sink_session(df: DataFrame, identify, table_name):
    schema = identify["schema"]
    table = f"{schema}.{table_name}"

    df.write.format("jdbc") \
        .option("url", identify["url"]) \
        .option("dbtable", table) \
        .option("user", identify["user"]) \
        .option("password", identify["password"]) \
        .option("driver", "org.postgresql.Driver") \
        .mode("append") \
        .save()