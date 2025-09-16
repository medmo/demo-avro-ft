package fr.francetravail.eda.demo;

import fr.ft.evenement.UserUpdated;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

public class Consumer {

    private static final Logger logger = java.util.logging.Logger.getLogger(Consumer.class.getName());

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", StringDeserializer.class);
        properties.put("value.deserializer", KafkaAvroDeserializer.class);

        properties.put("group.id", "my-consumer");
        properties.setProperty("auto.offset.reset", "earliest");
        properties.put("schema.registry.url", "http://localhost:8081");
        properties.put("specific.avro.reader", "true");
        properties.put("enable.auto.commit", "false");
        properties.put("auto.register.schema", "false");


        KafkaConsumer<String, UserUpdated> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("demo_java_ft"));

        while (true){
            logger.info("Polling");
            ConsumerRecords<String, UserUpdated> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, UserUpdated> record : records){
                UserUpdated userUpdated = record.value();
                logger.info("Key: " + record.key() + ", Value: " + userUpdated.toString());
                logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());
            }
            consumer.commitSync();
        }

    }
}
