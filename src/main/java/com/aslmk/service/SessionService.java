package com.aslmk.service;

import com.aslmk.model.Session;
import com.aslmk.model.User;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;

public interface SessionService {
    void saveSession(HttpSession session, User user);
    Session findById(String sessionId);
    void deleteSession(String sessionId);
    void deleteExpiredSessions(LocalDateTime expiresAt);
}
