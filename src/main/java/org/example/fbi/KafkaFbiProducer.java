package org.example.fbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.fbi.config.KafkaProducerConfig;
import org.example.util.ApiDataFetcher;

import java.util.List;

public class KafkaFbiProducer {
    private static final String API_URL = "https://api.fbi.gov/wanted/v1/list"; // Replace with your chosen API URL
    private static final String TOPIC_NAME = "uk_topic";
    private static final ObjectMapper objectMapper = new ObjectMapper(); // For converting objects to JSON

    public static void main(String[] args) {
        // Create Kafka Producer
        KafkaProducer<String, MostWantedCrime> producer = KafkaProducerConfig.createProducer();
        int i = 0;
            try {
                // Fetch crimes from the API
                List<MostWantedCrime> mostWantedCrimes = ApiDataFetcher.fetchCrimes(API_URL);
                System.out.println("size"  + mostWantedCrimes.size());

                // Send each crime as a separate message to Kafka
                for (MostWantedCrime mostWantedCrime : mostWantedCrimes) {
                    // Convert Crime object to JSON string
                    // Create and send a ProducerRecord to Kafka
//                    String crimeJson = objectMapper.writeValueAsString(crime);
                    ProducerRecord<String, MostWantedCrime> record = new ProducerRecord<>(TOPIC_NAME, mostWantedCrime.getUid(), mostWantedCrime);
                    producer.send(record);
                    System.out.println("Crime sent to Kafka topic: " + TOPIC_NAME + " " + i + " " + mostWantedCrime.getUid());
                    i++;
                }

                // Sleep for a while before fetching data again (adjust interval as needed)
                Thread.sleep(10000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Close the producer (optionally, if you're not running it indefinitely)
        // producer.close();
}

