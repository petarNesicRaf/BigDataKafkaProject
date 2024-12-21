package org.example.uk_crime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)  // This will ignore any unknown fields in the JSON response
public class Crime {
    @JsonProperty("category")
    private String category;
    @JsonProperty("location_type")
    private String locationType;
    @JsonProperty("location")
    private Location location;
    @JsonProperty("context")
    private String context;
    @JsonProperty("outcome_status")
    private OutcomeStatus outcomeStatus;
    @JsonProperty("persistent_id")
    private String persistentId;
    @JsonProperty("id")
    private long id;
    @JsonProperty("location_subtype")
    private String locationSubtype;
    @JsonProperty("month")
    private String month;

    @Getter
    @Setter
    public static class Location {
        @JsonProperty("latitude")
        private String latitude;
        @JsonProperty("longitude")
        private String longitude;
        @JsonProperty("street")
        private Street street;

    }

    @Getter
    @Setter
    public static class Street {
        @JsonProperty("id")
        private long id;
        @JsonProperty("name")
        private String name;
    }

    @Getter
    @Setter
    public static class OutcomeStatus {
        @JsonProperty("category")
        private String category;
        @JsonProperty("date")
        private String date;
    }

}