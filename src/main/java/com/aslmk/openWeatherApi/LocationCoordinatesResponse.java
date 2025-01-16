package com.aslmk.openWeatherApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class LocationCoordinatesResponse {
    @JsonProperty("coord")
    private Coord coord;

    @Getter
    @Setter
    public static class Coord {
        @JsonProperty("lon")
        private BigDecimal lon;
        @JsonProperty("lat")
        private BigDecimal lat;
    }
}
