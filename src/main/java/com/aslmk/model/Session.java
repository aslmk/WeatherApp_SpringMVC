package com.aslmk.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Sessions")
public class Session {
    @Id
    private String id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userid", referencedColumnName = "ID")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresat;
}
