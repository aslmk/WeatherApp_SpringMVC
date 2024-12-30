package com.aslmk.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Locations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private BigDecimal latitude;
    private BigDecimal longitude;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "userid", referencedColumnName = "ID")
    private Users user;
}
