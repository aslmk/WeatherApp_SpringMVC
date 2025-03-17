package com.aslmk;

import com.aslmk.dto.UserDto;
import com.aslmk.exception.InvalidCredentialsException;
import com.aslmk.exception.UserAlreadyExistsException;
import com.aslmk.repository.UserRepository;
import com.aslmk.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthorizationControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registration_shouldRedirectToLoginPage_whenUserRegisteredSuccessfully() throws Exception {
        UserDto userDto = new UserDto();
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register/save")
                    .param("login", "testUser")
                    .param("password", "testPassword")
                    .flashAttr("user", userDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

    @Test
    void registration_shouldReturnUserAlreadyExistsException_whenUserWithThisLoginAlreadyInDb() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("testPassword");

        UserDto userDto2 = new UserDto();
        userDto2.setUsername("testUser");
        userDto2.setPassword("testPassword2");

        userService.saveUser(userDto);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.saveUser(userDto2));
    }

    @Test
    void saveUser_shouldSaveUserToDb_whenUserProvidesValidCredentials() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("testPassword");
        userService.saveUser(userDto);
        Assertions.assertNotNull(userService.findByLogin(userDto.getUsername()));
    }

    @Test
    void saveUser_shouldReturnInvalidCredentialsException_whenUserProvidesInvalidCredentials() {
        UserDto userDto = new UserDto();
        userDto.setUsername("ts");
        userDto.setPassword("");
        Assertions.assertThrows(InvalidCredentialsException.class, () -> userService.saveUser(userDto));
    }

}
