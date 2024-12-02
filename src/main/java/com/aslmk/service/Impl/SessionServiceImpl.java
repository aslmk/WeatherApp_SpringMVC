package com.aslmk.service.Impl;

import com.aslmk.dto.UsersDto;
import com.aslmk.model.Sessions;
import com.aslmk.model.Users;
import com.aslmk.repository.SessionRepository;
import com.aslmk.service.SessionService;
import com.aslmk.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionServiceImpl implements SessionService {
    private SessionRepository sessionRepository;


    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }


    @Override
    public void saveSession(HttpSession session, Users user) {
        Sessions sessions = new Sessions();
        sessions.setId(session.getId());
        sessions.setUser(user);
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
        sessions.setExpiresat(expiresAt);
        sessionRepository.save(sessions);

    }

    @Override
    public Sessions findById(String sessionId) {
        return sessionRepository.findById(sessionId).orElse(null);
    }
}
