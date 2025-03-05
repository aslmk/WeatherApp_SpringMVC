package com.aslmk.controller;

import com.aslmk.dto.UserDto;
import com.aslmk.exception.InvalidCredentialsException;
import com.aslmk.exception.UserAlreadyExistsException;
import com.aslmk.model.User;
import com.aslmk.service.SessionService;
import com.aslmk.service.UserService;
import com.aslmk.util.CookieUtil;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthorizationController {
    private final UserService userService;
    private final SessionService sessionService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthorizationController(UserService userService,
                                   SessionService sessionService,
                                   PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/register")
    public String registration(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);

        return "registration";
    }

    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") UserDto user, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        if (session != null) {
            session.invalidate();
        }

        userService.saveUser(user);
        return "redirect:/auth/login";
    }


    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "login";
    }

    @PostMapping("/login/save")
    public String login(@ModelAttribute("user") UserDto user,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Model model) {

        Optional<User> userDB = userService.findByLogin(user.getLogin());

        if (userDB.isPresent() && passwordEncoder.matches(user.getPassword(), userDB.get().getPassword())) {
            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession newSession = request.getSession(true);
            sessionService.saveSession(newSession, userDB.get());
            newSession.setAttribute("userName", user.getLogin());

            CookieUtil.createCookie(response,
                    "SESSION_ID",
                    newSession.getId(),
                    24 * 60 * 60); // 24 hours

            return "redirect:/locations";

        }

        model.addAttribute("error", "Invalid username or password!");
       return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            sessionService.deleteSession(session.getId());
            session.invalidate();
        }

        CookieUtil.invalidateCookie(response, "SESSION_ID");

        return "redirect:/auth/login";
    }


}
