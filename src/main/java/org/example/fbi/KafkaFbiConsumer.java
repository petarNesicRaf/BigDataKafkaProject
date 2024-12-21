package org.example.fbi;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.serialization.FbiCrimeDeserializer;

import java.io.FileWriter;
import java.util.Collections;
import java.util.Properties;

public class KafkaFbiConsumer {
    private static final String TOPIC = "csvtopic"; // Replace with your Kafka topic
    private static final String BOOTSTRAP_SERVERS = "localhost:9092"; // Replace with your Kafka brokers
    private static final String GROUP_ID = "print-consumer-group";
    private static final String CSV_FILE_PATH = "csvaa.csv";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, FbiCrimeDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        Consumer<String, MostWantedCrime> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC));


        System.out.println("Kafka Consumer started. Listening for messages...");

        // Poll for messages and print them
        try (FileWriter fileWriter = new FileWriter(CSV_FILE_PATH);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT
                     .withHeader("UID", "Description", "Subject", "Status", "Reward Text", "Publication", "Sex", "Race", "Hair", "Age Range", "Weight", "Place of Birth", "Warning Message", "Complexion", "Nationality", "Aliases", "Image Original"))) {
            int i = 1;
            while (true) {
                ConsumerRecords<String, MostWantedCrime> records = consumer.poll(100); // Adjust the poll timeout as needed
                for (ConsumerRecord<String, MostWantedCrime> record : records) {
                    MostWantedCrime mostWantedCrime = record.value();

                    // Retrieve required fields from Crime object
                    String uid = mostWantedCrime.getUid();
                    String description = mostWantedCrime.getDescription();
                    String subject = mostWantedCrime.getSubject();
                    String status = mostWantedCrime.getStatus();
                    //String caution = crime.getCaution();
                    String rewardText = mostWantedCrime.getRewardText();
                    String publication = mostWantedCrime.getPublication() != null ? mostWantedCrime.getPublication().toString() : ""; // Convert to string if needed
                    String sex = mostWantedCrime.getSex();
                    String race = mostWantedCrime.getRace();
                    String hair = mostWantedCrime.getHair();
                    String ageRange = mostWantedCrime.getAgeRange();
                    String weight = mostWantedCrime.getWeight();
                    String placeOfBirth = mostWantedCrime.getPlaceOfBirth();
                    String warningMessage = mostWantedCrime.getWarningMessage();
                    String complexion = mostWantedCrime.getComplexion();
                    String nationality = mostWantedCrime.getNationality();

                    // Handling aliases (List<String>)
                    String aliases = mostWantedCrime.getAliases() != null ? String.join("; ", mostWantedCrime.getAliases()) : "";

                    // Handling images (List<Crime.Image>)
                    String imageOriginal = mostWantedCrime.getImages() != null && !mostWantedCrime.getImages().isEmpty() ? mostWantedCrime.getImages().get(0).getOriginal() : "";

                    // Write to CSV
                    System.out.println(i+"  " +uid);
                    csvPrinter.printRecord(i,uid, description, subject, status,  rewardText, publication, sex, race, hair, ageRange, weight, placeOfBirth, warningMessage, complexion, nationality, aliases, imageOriginal);
                    csvPrinter.flush();
                    i++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
