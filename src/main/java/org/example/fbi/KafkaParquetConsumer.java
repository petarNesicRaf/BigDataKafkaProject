package org.example.fbi;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.example.serialization.FbiCrimeDeserializer;

import java.util.Collections;
import java.util.Properties;

public class KafkaParquetConsumer {

    private static final String TOPIC = "topicjson"; // Replace with your Kafka topic
    private static final String BOOTSTRAP_SERVERS = "localhost:9092"; // Replace with your Kafka brokers
    private static final String GROUP_ID = "print-consumer-group";
    private static final String PARQUET_FILE_PATH = "xx.parquet";

    public static void main(String[] args) {
        // Set up Kafka consumer properties
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, FbiCrimeDeserializer.class.getName()); // Custom deserializer
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        Consumer<String, MostWantedCrime> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC));

        System.out.println("Kafka Consumer started. Listening for messages...");

        // Define Avro schema
        String schemaString = "{\"namespace\": \"example.avro\", " +
                "\"type\": \"record\", " +
                "\"name\": \"Crime\", " +
                "\"fields\": [" +
                "{\"name\": \"UID\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"Description\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"Subject\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"Status\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"RewardText\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"Publication\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"Sex\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"Race\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"Hair\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"AgeRange\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"Weight\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"PlaceOfBirth\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"WarningMessage\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"Complexion\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"Nationality\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"Aliases\", \"type\": [\"string\", \"null\"]}," +
                "{\"name\": \"ImageOriginal\", \"type\": [\"string\", \"null\"]}" +
                "]}";
        Schema schema = new Schema.Parser().parse(schemaString);

        // Create a Parquet writer
        Path path = new Path(PARQUET_FILE_PATH);
        try (ParquetWriter<GenericRecord> parquetWriter = AvroParquetWriter.<GenericRecord>builder(path)
                .withSchema(schema)
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .withConf(new Configuration())
                .build()) {

            // Poll for messages and write to Parquet
            while (true) {
                ConsumerRecords<String, MostWantedCrime> records = consumer.poll(100); // Adjust the poll timeout as needed
                for (ConsumerRecord<String, MostWantedCrime> record : records) {
                    MostWantedCrime mostWantedCrime = record.value();

                    // Create Avro generic record
                    GenericRecord avroRecord = new GenericData.Record(schema);
                    avroRecord.put("UID", mostWantedCrime.getUid());
                    avroRecord.put("Description", mostWantedCrime.getDescription());
                    avroRecord.put("Subject", mostWantedCrime.getSubject());
                    avroRecord.put("Status", mostWantedCrime.getStatus());
                    avroRecord.put("RewardText", mostWantedCrime.getRewardText());
                    avroRecord.put("Publication", mostWantedCrime.getPublication() != null ? mostWantedCrime.getPublication().toString() : null);
                    avroRecord.put("Sex", mostWantedCrime.getSex());
                    avroRecord.put("Race", mostWantedCrime.getRace());
                    avroRecord.put("Hair", mostWantedCrime.getHair());
                    avroRecord.put("AgeRange", mostWantedCrime.getAgeRange());
                    avroRecord.put("Weight", mostWantedCrime.getWeight());
                    avroRecord.put("PlaceOfBirth", mostWantedCrime.getPlaceOfBirth());
                    avroRecord.put("WarningMessage", mostWantedCrime.getWarningMessage());
                    avroRecord.put("Complexion", mostWantedCrime.getComplexion());
                    avroRecord.put("Nationality", mostWantedCrime.getNationality());

                    // Handling aliases (List<String>)
                    avroRecord.put("Aliases", mostWantedCrime.getAliases() != null ? String.join("; ", mostWantedCrime.getAliases()) : "");

                    // Handling images (List<Crime.Image>)
                    avroRecord.put("ImageOriginal", mostWantedCrime.getImages() != null && !mostWantedCrime.getImages().isEmpty() ? mostWantedCrime.getImages().get(0).getOriginal() : "");
                    System.out.println("avro" + avroRecord.toString());
                    // Write to Parquet file
                    parquetWriter.write(avroRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
