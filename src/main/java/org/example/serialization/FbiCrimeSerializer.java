package org.example.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.example.fbi.MostWantedCrime;

public class FbiCrimeSerializer implements Serializer<MostWantedCrime>{
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String s, MostWantedCrime data) {
        try {
            // If the data is null, return null
            if (data == null) {
                return null;
            }
            // Convert the Crime object to a byte array
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            // Log the error and return null in case of serialization failure
            e.printStackTrace();
            return null;
        }    }

}
