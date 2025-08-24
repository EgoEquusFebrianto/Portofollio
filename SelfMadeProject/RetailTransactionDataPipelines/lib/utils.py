from pyspark.sql import SparkSession

def get_spark_session(spark_conf):
    app = (
        SparkSession.builder
            .config(conf=spark_conf)
            .getOrCreate()
    )

    return app