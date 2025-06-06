package com.aslmk.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "Locations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    @Column(nullable = false, unique = true)
    private BigDecimal latitude;
    @Column(nullable = false, unique = true)
    private BigDecimal longitude;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userid", referencedColumnName = "ID")
    private User user;
}
