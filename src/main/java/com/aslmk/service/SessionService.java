package com.aslmk.service;

import com.aslmk.model.Sessions;
import com.aslmk.model.Users;
import jakarta.servlet.http.HttpSession;

public interface SessionService {
    void saveSession(HttpSession session, Users user);
    Sessions findById(String sessionId);
    Sessions getValidSession(String sessionId);
    void deleteSession(String sessionId);
}
