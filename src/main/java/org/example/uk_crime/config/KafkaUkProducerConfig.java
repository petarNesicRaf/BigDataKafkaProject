package org.example.uk_crime.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.fbi.MostWantedCrime;
import org.example.serialization.FbiCrimeSerializer;
import org.example.serialization.UkCrimeSerializer;
import org.example.uk_crime.Crime;

import java.util.Properties;

public class KafkaUkProducerConfig {

    public static KafkaProducer<String, Crime> createProducer(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UkCrimeSerializer.class);

        return new KafkaProducer<>(props);
    }
}
