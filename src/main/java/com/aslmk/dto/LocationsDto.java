package com.aslmk.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LocationsDto {
    private int id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;

}
