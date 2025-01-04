package com.aslmk.controller;

import com.aslmk.dto.UsersDto;
import com.aslmk.model.Sessions;
import com.aslmk.model.Users;
import com.aslmk.service.SessionService;
import com.aslmk.service.UsersService;
import com.aslmk.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthorizationController {
    private UsersService usersService;
    private SessionService sessionService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private HttpSession httpSession;

    public AuthorizationController(UsersService usersService, SessionService sessionService) {
        this.usersService = usersService;
        this.sessionService = sessionService;
    }


    @GetMapping("/register")
    public String registration(Model model) {
        UsersDto usersDto = new UsersDto();
        model.addAttribute("user", usersDto);

        return "registration";
    }

    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") UsersDto user, HttpServletRequest request, Model model) {
        try {
            HttpSession session = request.getSession();

            if (session != null) {
                session.invalidate();
            }

            usersService.saveUser(user);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed");
        }
        return "registration";
    }


    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new UsersDto());
        return "login";
    }

    @PostMapping("/login/save")
    public String login(@ModelAttribute("user") UsersDto user,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Model model) {

        Users userDB = usersService.findByLogin(user.getLogin());

        if (userDB != null && passwordEncoder.matches(user.getPassword(), userDB.getPassword())) {
            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession newSession = request.getSession(true);
            sessionService.saveSession(newSession, userDB);

            Cookie cookie = new Cookie("SESSION_ID", newSession.getId());
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return "redirect:/locations";

        }

        model.addAttribute("error", "Invalid username or password!");
       return "login";
    }

}
