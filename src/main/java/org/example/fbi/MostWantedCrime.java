package org.example.fbi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)  // This will ignore any unknown fields in the JSON response
public class MostWantedCrime {
    @JsonProperty("uid")
    private String uid;

    @JsonProperty("description")
    private String description;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("status")
    private String status;

    @JsonProperty("caution")
    private String caution;

    @JsonProperty("aliases")
    private List<String> aliases;  // Updated to List<String>

    @JsonProperty("reward_text")
    private String rewardText;

    @JsonProperty("publication")
    private String publication;  // Assuming it's a date/time in ISO 8601 format

    @JsonProperty("sex")
    private String sex;

    @JsonProperty("race")
    private String race;

    @JsonProperty("hair")
    private String hair;

    /*
    @JsonProperty("height_max")
    private float heightMax;


     */
    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("age_range")
    private String ageRange;

    @JsonProperty("weight")
    private String weight;

    @JsonProperty("dates_of_birth_used")
    private List<String> datesOfBirthUsed;  // Updated to List<String>

    @JsonProperty("place_of_birth")
    private String placeOfBirth;

    @JsonProperty("languages")
    private List<String> languages;  // Updated to List<String>

    @JsonProperty("warning_message")
    private String warningMessage;

    @JsonProperty("occupations")
    private List<String> occupations;

    @JsonProperty("complexion")
    private String complexion;

    @JsonProperty("images")
    private List<Image> images;

    // Inner class for Image
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        @JsonProperty("original")
        private String original;

        @JsonProperty("thumb")
        private String thumb;

        @JsonProperty("large")
        private String large;

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }
    }

    // Getters and setters for all fields

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getRewardText() {
        return rewardText;
    }

    public void setRewardText(String rewardText) {
        this.rewardText = rewardText;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }
/*
    public float getHeightMax() {
        return heightMax;
    }

    public void setHeightMax(float heightMax) {
        this.heightMax = heightMax;
    }

 */
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public List<String> getDatesOfBirthUsed() {
        return datesOfBirthUsed;
    }

    public void setDatesOfBirthUsed(List<String> datesOfBirthUsed) {
        this.datesOfBirthUsed = datesOfBirthUsed;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public List<String> getOccupations() {
        return occupations;
    }

    public void setOccupations(List<String> occupations) {
        this.occupations = occupations;
    }

    public String getComplexion() {
        return complexion;
    }

    public void setComplexion(String complexion) {
        this.complexion = complexion;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
