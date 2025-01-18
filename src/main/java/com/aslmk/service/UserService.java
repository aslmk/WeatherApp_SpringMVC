package com.aslmk.service;

import com.aslmk.dto.UserDto;
import com.aslmk.model.User;

import java.util.Optional;


public interface UserService {
    void saveUser(UserDto userDto);

    Optional<User>  findByLogin(String login);
}
