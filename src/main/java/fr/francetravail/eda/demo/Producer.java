package fr.francetravail.eda.demo;

import fr.ft.evenement.UserAction;
import fr.ft.evenement.UserProfile;
import fr.ft.evenement.UserUpdated;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.logging.Logger;

public class Producer {

    private static final Logger logger = Logger.getLogger(Producer.class.getName());


    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");

        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", KafkaAvroSerializer.class);
        properties.put("schema.registry.url", "http://localhost:8081");

        UserUpdated.Builder builder = UserUpdated.newBuilder();
        UserProfile.Builder profile = UserProfile.newBuilder();
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setEmail("john.doe@test.sn");
        profile.setAge(30);

        builder.setUserId("1234");
        builder.setAction(UserAction.CREATED);
        builder.setProfile(profile.build());

        UserUpdated userUpdated = builder.build();

        KafkaProducer<String, UserUpdated> producer = new KafkaProducer<>(properties);

        ProducerRecord<String, UserUpdated> record = new ProducerRecord<>("demo_java_ft", userUpdated);

        producer.send(record, (recordMetadata, e) -> {
            if (e == null) {
                logger.info("Received new metadata. \n" +
                    "Topic: " + recordMetadata.topic() + "\n" +
                    "Partition: " + recordMetadata.partition() + "\n" +
                    "Offset: " + recordMetadata.offset() + "\n" +
                    "Timestamp: " + recordMetadata.timestamp());
            } else {
                logger.warning("Error while producing: " + e);
            }
        });

        producer.flush();
        producer.close();

    }

}
