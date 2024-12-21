package org.example.fbi.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.fbi.MostWantedCrime;
import org.example.serialization.FbiCrimeSerializer;

import java.util.Properties;

public class KafkaProducerConfig {


    public static KafkaProducer<String, MostWantedCrime> createProducer(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, FbiCrimeSerializer.class);

        return new KafkaProducer<>(props);
    }
}
