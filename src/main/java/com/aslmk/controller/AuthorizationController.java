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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthorizationController {
    private UsersService usersService;
    private SessionService sessionService;


    public AuthorizationController(UsersService usersService, SessionService sessionService) {
        this.usersService = usersService;
        this.sessionService = sessionService;
    }


    @GetMapping("/register")
    public String registration(Model model, HttpServletRequest request) {
        UsersDto usersDto = new UsersDto();
        model.addAttribute("user", usersDto);

        return "registration";
    }

    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") UsersDto user,  HttpServletRequest request) {

        Users userEntity = usersService.saveUser(user);

        HttpSession session = request.getSession();
        sessionService.saveSession(session, userEntity);

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

        HttpSession httpSession = request.getSession(false);
        Sessions dbSession = null;

        if (httpSession != null) {
            dbSession = sessionService.findById(httpSession.getId());
        } else {
            String cookieSession = CookieUtil.getSessionIdFromCookie(request);
            if (cookieSession != null) {
                dbSession = sessionService.findById(cookieSession);
            }
        }

        if (dbSession != null) {
            Users userEntity = dbSession.getUser();

            if (user.getPassword().equals(userEntity.getPassword()) &&
                    user.getLogin().equals(userEntity.getLogin())) {

                if (httpSession == null) {

                    httpSession = request.getSession(true);
                    sessionService.saveSession(httpSession, userEntity);

                    Cookie cookie = new Cookie("SESSION_ID", httpSession.getId());
                    cookie.setMaxAge(25 * 60 * 60);
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                }

                return "redirect:/locations";
            } else {
                model.addAttribute("error", "Invalid username or password");
            }
        } else {
            model.addAttribute("error", "Session not found.");
        }

        return "login";
    }

}
