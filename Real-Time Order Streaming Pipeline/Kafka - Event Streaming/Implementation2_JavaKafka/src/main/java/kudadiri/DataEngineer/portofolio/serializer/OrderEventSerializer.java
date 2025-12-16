package kudadiri.DataEngineer.portofolio.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import kudadiri.DataEngineer.portofolio.model.OrderEvent;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class OrderEventSerializer implements Serializer<OrderEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, OrderEvent orderEvent) {
        try {
            return objectMapper.writeValueAsBytes(orderEvent);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing order ", e);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, OrderEvent data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
