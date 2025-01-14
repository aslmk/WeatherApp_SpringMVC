package com.aslmk.service.Impl;

import com.aslmk.dto.UsersDto;
import com.aslmk.exception.UserAlreadyExistsException;
import com.aslmk.model.Users;
import com.aslmk.repository.UsersRepository;
import com.aslmk.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(UsersDto usersDto) throws UserAlreadyExistsException {
        try {
            Users users = new Users();
            users.setLogin(usersDto.getLogin());
            users.setPassword(passwordEncoder.encode(usersDto.getPassword()));
            usersRepository.save(users);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User already exists.");
        }

    }

    @Override
    public Users findByLogin(String login) {
        return usersRepository.findByLogin(login);
    }
}
