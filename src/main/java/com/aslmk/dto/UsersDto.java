package com.aslmk.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;



public class UsersDto {
    private int id;
    private String login;
    private String password;
}
