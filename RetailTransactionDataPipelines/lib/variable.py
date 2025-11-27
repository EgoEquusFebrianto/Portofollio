from pyspark import SparkConf
from configparser import ConfigParser

class VariableManager:
    def __init__(self, variable):
        self.cluster = variable[1]
        self.host = variable[2]
        self.port = variable[3]
        self.user = variable[4]
        self.password = variable[5]
        self.database = variable[6]
        self.schema = variable[7]

    def get_spark_config(self):
        mode = {"local": "SPARK_LOCAL", "yarn_env": "SPARK_YARN"}
        spark_conf, config = SparkConf(), ConfigParser()
        environment = mode[self.cluster]
        config.read("Settings/SparkConf.conf")

        for (key, value) in config.items(environment):
            spark_conf.set(key, value)

        return spark_conf

    def get_database_identify(self):
        url = f"jdbc:postgresql://{self.host}:{self.port}/{self.database}"

        data = {
            "url": url,
            "user": self.user,
            "password": self.password,
            "schema": self.schema,
        }

        return data