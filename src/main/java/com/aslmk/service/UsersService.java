package com.aslmk.service;

import com.aslmk.dto.UsersDto;
import com.aslmk.model.Users;


public interface UsersService {
    Users saveUser(UsersDto usersDto);
}
