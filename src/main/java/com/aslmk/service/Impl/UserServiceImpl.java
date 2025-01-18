package com.aslmk.service.Impl;

import com.aslmk.dto.UserDto;
import com.aslmk.exception.UserAlreadyExistsException;
import com.aslmk.model.User;
import com.aslmk.repository.UserRepository;
import com.aslmk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(UserDto userDto) throws UserAlreadyExistsException {
        try {
            User user = new User();
            user.setLogin(userDto.getLogin());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User already exists.");
        }

    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
