package com.aslmk;

import com.aslmk.dto.UsersDto;
import com.aslmk.model.Users;
import com.aslmk.repository.UsersRepository;
import com.aslmk.service.UsersService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
    private UsersService usersService;


    @Test
    void registrationSuccessTest() throws Exception {
        UsersDto userDto = new UsersDto();
        userDto.setLogin("testUser");
        userDto.setPassword("testPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/register/save")
                    .param("login", "testUser")
                    .param("password", "testPassword")
                    .flashAttr("user", userDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        Users user = usersService.findByLogin(userDto.getLogin());
        Assertions.assertNotNull(user);
        Assertions.assertEquals(userDto.getLogin(), user.getLogin());
    }
}
