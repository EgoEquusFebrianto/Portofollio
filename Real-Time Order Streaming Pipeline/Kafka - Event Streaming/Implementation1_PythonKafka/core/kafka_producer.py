from confluent_kafka import Producer
import logging

class KafkaProducer:
    def __init__(self, config, key_serializer= None, value_serializer= None):
        self.logger = logging.getLogger("KafkaProducer")

        self.key_serializer = key_serializer
        self.value_serializer = value_serializer
        self.producer = Producer(config)

    def delivery_report(self, err, msg):
        if err is not None:
            self.logger.error(f"Delivery Failed: {err}")
        else:
            self.logger.info(
                f"Delivery to {msg.topic()} [{msg.partition()}] @ offset {msg.offset()}"
            )

    def send(self, topic, key= None, value= None):
        if self.key_serializer:
            key = self.key_serializer(key)
        
        if self.value_serializer:
            value = self.value_serializer(value)
        
        try:
            self.producer.produce(
                topic= topic,
                key= key,
                value= value,
                callback= self.delivery_report
            )

            self.producer.poll(0.01)
        
        except BufferError:
            self.logger.warning("Buffer Full, Flushing...")
            self.producer.flush()
            self.producer.produce(
                topic= topic,
                key= key,
                value= value,
                callback= self.delivery_report
            )
        
        except KeyboardInterrupt:
            self.logger.info("Keyboard Interrupted Detect, Flushing...")
            self.producer.flush()
    
    def flush(self):
        self.logger.info("Delivery Pending Messages...")
        self.producer.flush()