from pyspark.sql import SparkSession

class Log4j2:
    def __init__(self, spark: SparkSession):
        conf = spark.sparkContext.getConf()
        app_name = conf.get("spark.app.name")
        root_class = "engineering.kudadiri.spark.examples"
        header = f"{root_class}.{app_name}"
        self.logger = spark._jvm.org.apache.logging.log4j.LogManager.getLogger(header)

    def info(self, message):
        self.logger.info(message)

    def warn(self, message):
        self.logger.warn(message)

    def error(self, message):
        self.logger.error(message)