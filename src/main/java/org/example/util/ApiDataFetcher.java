package org.example.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.fbi.MostWantedCrime;
import org.example.uk_crime.Crime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiDataFetcher {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String API_URL_TEMPLATE = "https://data.police.uk/api/crimes-street/all-crime?lat=%f&lng=%f&date=2023-03";

    public static List<MostWantedCrime> fetchCrimes(String apiUrl) {
        List<MostWantedCrime> mostWantedCrimes = new ArrayList<>();

        try {
            String jsonResponse = fetchData(apiUrl);

            // Parse the response to get the "items" array
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode itemsNode = rootNode.get("items");

            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    MostWantedCrime mostWantedCrime = objectMapper.treeToValue(itemNode, MostWantedCrime.class);
                    mostWantedCrimes.add(mostWantedCrime);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mostWantedCrimes;
    }

    public static List<Crime> fetchUkCrimes(String apiURL){
        List<Crime> crimes = new ArrayList<>();

        try{
            String jsonResponse = fetchData(apiURL);
            JsonNode listResponse = objectMapper.readTree(jsonResponse);
            if(listResponse.isArray()){
                for(JsonNode itemNode : listResponse){
                    Crime crime = objectMapper.treeToValue(itemNode, Crime.class);
                    crimes.add(crime);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return crimes;
    }


    public static String fetchData(String apiUrl){
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(apiUrl);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            // Convert response entity to string
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
