package org.example.uk_crime.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.fbi.MostWantedCrime;
import org.example.serialization.FbiCrimeDeserializer;
import org.example.serialization.UkCrimeDeserializer;
import org.example.uk_crime.Crime;

import java.util.Properties;

public class KafkaUkConsumerConfig {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092"; // Replace with your Kafka brokers
    private static final String GROUP_ID = "print-consumer-group";

    public static Consumer<String, Crime> createConsumer(){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UkCrimeDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        Consumer<String, Crime> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        return consumer;
    }
}
