package com.aslmk.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocationsDto {
    private int id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;

}
