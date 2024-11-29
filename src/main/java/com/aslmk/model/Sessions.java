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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = Users.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "UserId", referencedColumnName = "ID")
    private Users user;

    private LocalDateTime expiresAt;
}
