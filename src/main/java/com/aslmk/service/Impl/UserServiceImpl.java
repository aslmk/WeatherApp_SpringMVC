package com.aslmk.service.Impl;

import com.aslmk.dto.UserDto;
import com.aslmk.exception.InvalidCredentialsException;
import com.aslmk.exception.UserAlreadyExistsException;
import com.aslmk.model.User;
import com.aslmk.repository.UserRepository;
import com.aslmk.service.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(UserDto userDto) throws UserAlreadyExistsException, InvalidCredentialsException {
        try {
            if (userDto.getPassword().length() < 3) {
                throw new InvalidCredentialsException("Invalid credentials.");
            }

            User user = new User();
            user.setLogin(userDto.getLogin());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("uniqueloginconstraint")) {
                throw new UserAlreadyExistsException("User already exists.");
            } else if (e.getMessage().contains("check_min_length")) {
                throw new InvalidCredentialsException("Invalid credentials.");
            }
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
