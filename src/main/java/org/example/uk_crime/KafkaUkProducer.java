package org.example.uk_crime;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.fbi.KafkaFbiProducer;
import org.example.uk_crime.config.KafkaUkProducerConfig;
import org.example.util.ApiDataFetcher;

import java.util.List;

public class KafkaUkProducer {
    private static final String API_URL = "https://data.police.uk/api/crimes-street/all-crime?lat=52.629729&lng=-1.131592&date=2023-01";
    private static final String TOPIC_NAME = "uktop";
    private static final String BASE_API_URL = "https://data.police.uk/api/crimes-street/all-crime";


    public static void main(String[] args) {
        // Define the bounding box for London
        double minLat = 51.286;
        double maxLat = 51.686;
        double minLng = -0.510;
        double maxLng = 0.334;

        // Define the step size for incrementing latitude and longitude
        double latStep = 0.05;
        double lngStep = 0.05;

        KafkaProducer<String, Crime> producer = KafkaUkProducerConfig.createProducer();

        try {
            // Iterate over latitude and longitude ranges
            for(int month=1; month<12;month++) {

                for (double lat = minLat; lat <= maxLat; lat += latStep) {
                    for (double lng = minLng; lng <= maxLng; lng += lngStep) {
                        String apiUrl = String.format("%s?lat=%f&lng=%f&date=2023-0%d", BASE_API_URL, lat, lng, month);
                        System.out.println("Fetching data from URL: " + apiUrl);

                        // Fetch data from the API
                        List<Crime> crimes = ApiDataFetcher.fetchUkCrimes(apiUrl);

                        System.out.println("Fetched " + crimes.size() + " crimes for coordinates: lat=" + lat + ", lng=" + lng);

                        // Send each crime to Kafka
                        int i = 1;
                        for (Crime crime : crimes) {
                            ProducerRecord<String, Crime> record = new ProducerRecord<>(TOPIC_NAME, String.valueOf(crime.getId()), crime);
                            producer.send(record);

                            System.out.println(i + ": Crime sent to Kafka topic " + TOPIC_NAME + " - ID: " + crime.getId() + ", Category: " + crime.getCategory());
                            i++;
                        }

                        // Optionally, add a delay between requests to avoid hitting API rate limits
                        Thread.sleep(100); // 1 second delay
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}

