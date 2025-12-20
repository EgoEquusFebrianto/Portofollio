package engineering.spark.streaming.utility

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.SparkConf

object SparkConfiguration {
  def getSparkConfig(cluster: String): SparkConf = {
    val mode = Map(
      "local_env" -> "local",
      "yarn_env" -> "yarn"
    )

    val env = mode(cluster)
    val config: Config = ConfigFactory.load()
    val sparkConf = new SparkConf()

    config.getConfig(s"spark.$env")
      .entrySet()
      .forEach { entry =>
        sparkConf.set(entry.getKey, entry.getValue.unwrapped().toString)
      }

    sparkConf
  }
}