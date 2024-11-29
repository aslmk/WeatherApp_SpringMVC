package com.aslmk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorizationController {

    @GetMapping("/register")
    public String registration() {
        return "registration";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
