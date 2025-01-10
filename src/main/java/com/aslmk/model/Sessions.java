package com.aslmk.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Sessions {
    @Id
    private String id;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "userid", referencedColumnName = "ID")
    private Users user;

    @Column(nullable = false)
    private LocalDateTime expiresat;
}
