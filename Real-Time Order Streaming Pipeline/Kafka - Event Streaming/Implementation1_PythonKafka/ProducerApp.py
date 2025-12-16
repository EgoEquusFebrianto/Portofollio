from utility.config_kafka import get_kafka_config
from utility.logging_config import setup_logging
from core.serializer import *
from core.kafka_producer import KafkaProducer
from lib.data_generator import generate_order_event
import time

if __name__ == "__main__":    
    path = "config/kafka_config.conf"
    mode = "LAB"

    setup_logging()
    configs = get_kafka_config(path, mode)
    key_serializer = string_serializer
    value_serializer = json_serializer

    producer = KafkaProducer(configs, key_serializer, value_serializer)

    # topics = "order-streaming-pipelines"
    topics = "testing"

    try:
        while True:
            event = generate_order_event()
            key = event["order"]["order_id"]

            producer.send(topics, key, event)
            
            time.sleep(1)
    except KeyboardInterrupt:
        producer.flush()
    