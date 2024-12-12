package com.aslmk.openWeatherApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LocationWeatherResponse {
    @JsonProperty("weather")
    private Weather[] weather;

    @Getter
    @Setter
    public static class Weather {
        @JsonProperty("id")
        private long id;
        @JsonProperty("main")
        private String main;
        @JsonProperty("description")
        private String description;
        @JsonProperty("icon")
        private String icon;
    }
}
