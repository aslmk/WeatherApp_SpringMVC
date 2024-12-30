package com.aslmk.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String login;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Locations> locations = new ArrayList<>();

}
