package com.aslmk.service;

import com.aslmk.dto.UsersDto;
import com.aslmk.model.Users;


public interface UsersService {
    void saveUser(UsersDto usersDto);

    Users findByLogin(String login);
}
