from lib.Database import data_sink_session
from lib.Logging import Log4j2
from lib.Transformation import *
from lib.utils import get_spark_session
from lib.variable import VariableManager
from lib.DataLoader import read_data
from datetime import datetime
import uuid
import sys

if __name__ == "__main__":
    if len(sys.argv) < 8:
        print("[Important] Usage variable, main.py <mode> <host> <port> <user> <password> <database> <schema>")
        sys.exit()

    job_run_id = f"Project-{uuid.uuid4()}"
    date = datetime.now().strftime("%d-%m-%Y %H:%M:%S")
    utils = VariableManager(sys.argv)

    spark_conf = utils.get_spark_config()
    identify = utils.get_database_identify()
    spark = get_spark_session(spark_conf)
    log = Log4j2(spark)

    log.info(f"Spark App with id-{job_run_id}, Retail Transaction Pipelines Running at {date}.")
    
    df = read_data(spark)
    log.info("[INFO] Success Read File..")
    df.printSchema()

    df_clean = data_cleaning_session(df)
    new_df = df_add_column(spark, df_clean)

    df_sales_per_SaC = agg_sales_per_store_and_category(new_df)
    daily_summary = agg_daily_summary(new_df)
    monthly_summary = agg_monthly_summary(new_df)

    try:
        log.info("[INFO] Sink Session Begin")
        data_sink_session(new_df, identify, "retail_transaction_dataset")
        data_sink_session(df_sales_per_SaC, identify, "sales_per_store_and_category")
        data_sink_session(daily_summary, identify, "daily_summary")
        data_sink_session(monthly_summary, identify, "monthly_summary")
        log.info("[INFO] Sink Session Done..")

    except Exception as e:
        log.error(f"Unexpected error happened at DATA SINK SESSION: {e}")

    log.info("Spark Terminate.")
    spark.stop()