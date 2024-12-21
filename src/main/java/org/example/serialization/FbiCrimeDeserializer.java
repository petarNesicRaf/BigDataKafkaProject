package org.example.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.example.fbi.MostWantedCrime;

public class FbiCrimeDeserializer implements Deserializer<MostWantedCrime> {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public MostWantedCrime deserialize(String topic, byte[] data) {
        try {
            // If data is null, return null to avoid errors
            if (data == null) {
                return null;
            }
            // Deserialize the byte array into a Crime object
            return objectMapper.readValue(data, MostWantedCrime.class);
        } catch (Exception e) {
            // Log the error and return null in case of deserialization failure
            e.printStackTrace();
            return null;
        }
    }
}
