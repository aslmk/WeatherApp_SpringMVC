package com.aslmk.service.Impl;

import com.aslmk.model.Session;
import com.aslmk.model.User;
import com.aslmk.repository.SessionRepository;
import com.aslmk.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;


    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }


    @Override
    public void saveSession(HttpSession session, User user) {
        Session sessions = new Session();
        sessions.setId(session.getId());
        sessions.setUser(user);
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
        sessions.setExpiresat(expiresAt);
        sessionRepository.save(sessions);

    }

    @Override
    public Optional<Session> findById(String sessionId) {
        return sessionRepository.findById(sessionId);
    }

    @Override
    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    @Override
    @Transactional
    public void deleteExpiredSessions(LocalDateTime expiresAt) {
        sessionRepository.deleteExpiredSessions(expiresAt);
    }
}
