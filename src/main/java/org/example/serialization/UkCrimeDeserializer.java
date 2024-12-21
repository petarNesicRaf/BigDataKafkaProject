package org.example.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.example.fbi.MostWantedCrime;
import org.example.uk_crime.Crime;

public class UkCrimeDeserializer implements Deserializer<Crime> {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Crime deserialize(String topic, byte[] data) {
        try {
            // If data is null, return null to avoid errors
            if (data == null) {
                return null;
            }
            // Deserialize the byte array into a Crime object
            return objectMapper.readValue(data, Crime.class);
        } catch (Exception e) {
            // Log the error and return null in case of deserialization failure
            e.printStackTrace();
            return null;
        }
    }
}
