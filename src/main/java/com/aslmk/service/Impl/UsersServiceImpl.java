package com.aslmk.service.Impl;

import com.aslmk.dto.UsersDto;
import com.aslmk.model.Users;
import com.aslmk.repository.UsersRepository;
import com.aslmk.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void saveUser(UsersDto usersDto) {
        Users users = new Users();
        users.setLogin(usersDto.getLogin());
        users.setPassword(passwordEncoder.encode(usersDto.getPassword()));
        usersRepository.save(users);
    }

    @Override
    public Users findByLogin(String login) {
        return usersRepository.findByLogin(login);
    }
}
