package com.aslmk.service.Impl;

import com.aslmk.exception.SessionNotFoundException;
import com.aslmk.model.Sessions;
import com.aslmk.model.Users;
import com.aslmk.repository.SessionRepository;
import com.aslmk.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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
    public Sessions findById(String sessionId) throws SessionNotFoundException {
        return sessionRepository.findById(sessionId)
                .orElseThrow(
                        () ->new SessionNotFoundException("Session not found"));
    }

    @Override
    public Sessions getValidSession(String sessionId) {
        Optional<Sessions> optionalSession = sessionRepository.findById(sessionId);

        if (optionalSession.isPresent()) {
            Sessions session = optionalSession.get();
            if (session.getExpiresat().isAfter(LocalDateTime.now())) {
                return session;
            } else {
                sessionRepository.deleteById(sessionId);
            }
        }
        return null;
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
