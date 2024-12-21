package org.example.uk_crime;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.example.uk_crime.config.KafkaUkConsumerConfig;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KafkaUkConsumer {
    private static final String TOPIC = "uktop";
    private static final String FULL_CSV_FILE_PATH = "general_crime_data.csv";
    private static final String LOCATION_CATEGORY_FILE_PATH = "location_crime_data.csv";
    private static final String CATEGORY_STREAM_FILE_PATH = "stream.csv";
    private static int fileCount = 1;  // Track the number of files created
    private static final int BATCH_SIZE = 10000;  // Number of records per CSV file
    private static int recordCounter = 0;  // Track the number of records written to the current file

    public static void main(String[] args) {
        System.out.println("Consumer initialising: ");
        Consumer<String, Crime> consumer = KafkaUkConsumerConfig.createConsumer();
        consumer.subscribe(Collections.singletonList(TOPIC));

        try (FileWriter fullFileWriter = new FileWriter(FULL_CSV_FILE_PATH, true);
                 CSVPrinter fullCsvPrinter = new CSVPrinter(fullFileWriter, CSVFormat.DEFAULT
                         .withHeader("ID", "Category", "Location Type", "Latitude", "Longitude", "Street ID", "Street Name", "Outcome Status", "Month"));

                 FileWriter locationCategoryWriter = new FileWriter(LOCATION_CATEGORY_FILE_PATH, true);
                 CSVPrinter locationCategoryPrinter = new CSVPrinter(locationCategoryWriter, CSVFormat.DEFAULT
                         .withHeader("Latitude", "Longitude", "Category"));


        ) {

            FileWriter categoryStreamWriter = new FileWriter(getStreamFilePath(fileCount),true);
            CSVPrinter categoryStreamPrinter = new CSVPrinter(categoryStreamWriter, CSVFormat.DEFAULT
                    .withHeader("Category", "Month"));
            int i = 1;
            while (true) {
                ConsumerRecords<String, Crime> records = consumer.poll(500); // Adjust the poll timeout as needed

                // Track offsets to commit
                Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();


                for (ConsumerRecord<String, Crime> record : records) {
                    Crime crime = record.value();

                    // Extracting values from the Crime object
                    String id = String.valueOf(crime.getId());
                    String category = crime.getCategory();
                    String locationType = crime.getLocationType();
                    String latitude = crime.getLocation().getLatitude();
                    String longitude = crime.getLocation().getLongitude();
                    long streetId = crime.getLocation().getStreet().getId();
                    String streetName = crime.getLocation().getStreet().getName();
                    String outcomeStatus = crime.getOutcomeStatus() != null ? crime.getOutcomeStatus().getCategory() : "Unknown";
                    String month = crime.getMonth();

                    try {
                        fullCsvPrinter.printRecord(id, category, locationType, latitude, longitude, streetId, streetName, outcomeStatus, month);
                        fullCsvPrinter.flush();

                        locationCategoryPrinter.printRecord(latitude,longitude,category);
                        locationCategoryPrinter.flush();

                        categoryStreamPrinter.printRecord(category, month);
                        categoryStreamPrinter.flush();
                        recordCounter++;

                        if(recordCounter>=BATCH_SIZE){
                            categoryStreamPrinter.close();;
                            recordCounter=0;
                            fileCount++;

                            categoryStreamWriter = new FileWriter(getStreamFilePath(fileCount), true);
                            categoryStreamPrinter = new CSVPrinter(categoryStreamWriter, CSVFormat.DEFAULT.withHeader("Category", "Month"));
                        }
                    }catch(Exception e){
                        System.err.println("Error writing record to CSV: " + e.getMessage());
                        e.printStackTrace();
                    }


                    System.out.println("Record consumed " + i);
                    i++;

                    // Add offset to commit map
                    TopicPartition partition = new TopicPartition(record.topic(), record.partition());
                    offsetsToCommit.put(partition, new OffsetAndMetadata(record.offset() + 1));
                }

                // Commit offsets after processing records
                if (!offsetsToCommit.isEmpty()) {
                    consumer.commitSync(offsetsToCommit);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
                //todo
            consumer.close();
        }
    }


    // Helper method to generate a dynamic file path for streaming data
    private static String getStreamFilePath(int fileCount) {
        return "stream/stream_" + fileCount + ".csv";
    }

}

