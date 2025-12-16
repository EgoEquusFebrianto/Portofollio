package kudadiri.DataEngineer.portofolio;

import kudadiri.DataEngineer.portofolio.generator.OrderEventGenerator;
import kudadiri.DataEngineer.portofolio.model.OrderEvent;
import kudadiri.DataEngineer.portofolio.serializer.OrderEventSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaProducerApp {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerApp.class.getSimpleName());

    public static void main(String[] args){

        if ("--version".equals(args[0])) {
            System.out.println("Kafka Producer App v1.0.0");
            System.out.println("Built with Kafka 3.8.0");

            return;
        } else if ( args.length < 3) {
            System.out.println("[IMPORTANT] Need Configuration Variables..");

            return;
        } else {
            System.out.printf("Access Granted, Welcome %s.", args[0]);
        }

//        String topic = "order-streaming-pipeline";
        String servers = args[1];
        String topic = args[2];

        Properties properties = new Properties();
        properties.put("bootstrap.servers", servers);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", OrderEventSerializer.class.getName());
        properties.put("transactional.id", "order-streaming-pipelines-Kudadiri");
        properties.put("compression.type", "snappy");

        log.info("Initializing Producer...");
        KafkaProducer<String, OrderEvent> producer = new KafkaProducer<>(properties);
        log.info("Initializing Success...");

        try {
            log.info("Producer Star Working...");

            // Init Transaction
            producer.initTransactions();
            int section = 1;

            while (true) {

                // Begin Transaction
                producer.beginTransaction();

                for (int i = 0; i < 10; i ++) {
                    OrderEvent data = OrderEventGenerator.generate();
                    String key = data.getOrder().getOrderId();

                    ProducerRecord<String, OrderEvent> record = new ProducerRecord<>(topic, key, data);

                    // Proceed to Sending Data
                    producer.send(record, (metadata, exception) -> {
                        if (exception != null) {
                            log.error("Failed to send record: Key={}, Value={}. Error: {}",
                                    record.key(), record.value(), exception.getMessage());
                        } else {
                            log.info("Successfully sent record: Key={}, Value={} | Partition={}, Offset={}",
                                    record.key(), record.value(), metadata.partition(), metadata.offset()
                            );
                        }
                    });
                }

                log.info("Committing transaction...");
                producer.commitTransaction();
                log.info("Transaction-[{}] committed successfully. All messages sent exactly once.", section);
            }
        } catch (Exception e) {
            log.error("Transaction Failed. Reason: {}", e.getMessage());
            log.info("Initializing Rollback...");
            producer.abortTransaction();
            log.error("Transaction aborted. No partial data will be written.");
        } finally {
            log.info("Closing producer...");
            producer.close();
            log.info("Producer closed gracefully");
        }
    }
}