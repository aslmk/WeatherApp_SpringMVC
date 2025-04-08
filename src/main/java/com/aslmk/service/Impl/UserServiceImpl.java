package com.aslmk.service.Impl;

import com.aslmk.dto.UserDto;
import com.aslmk.exception.InvalidCredentialsException;
import com.aslmk.exception.ServiceException;
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
    @Override
    public void saveUser(UserDto userDto) throws UserAlreadyExistsException, InvalidCredentialsException {
        try {
            User user = User.builder()
                    .login(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .build();
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            Throwable rootCause = e.getRootCause();
            if (rootCause instanceof ConstraintViolationException cve) {
                String constraintName = cve.getConstraintName();

                if (constraintName == null) {
                    throw new ServiceException("Unknown database constraint exception: " + e.getMessage());
                }

                switch (constraintName) {
                    case "uniqueloginconstraint":
                        throw new UserAlreadyExistsException("User already exists.");
                    case "check_min_length":
                        throw new InvalidCredentialsException("Invalid credentials.");
                }

            } else {
                throw new ServiceException("Unexpected error occurred while saving user: " + e.getMessage());
            }
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
