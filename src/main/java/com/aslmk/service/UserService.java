package com.aslmk.service;

import com.aslmk.dto.UserDto;
import com.aslmk.model.User;


public interface UserService {
    void saveUser(UserDto userDto);

    User findByLogin(String login);
}
