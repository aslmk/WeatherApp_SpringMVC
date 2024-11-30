package com.aslmk.controller;

import com.aslmk.dto.UsersDto;
import com.aslmk.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthorizationController {
    private UsersService usersService;


    public AuthorizationController(UsersService usersService) {
        this.usersService = usersService;
    }


    @GetMapping("/register")
    public String registration(Model model) {
        UsersDto usersDto = new UsersDto();
        model.addAttribute("user", usersDto);
        return "registration";
    }

    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") UsersDto user) {

        usersService.saveUser(user);

        return "registration";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
