package com.aslmk.scheduler;

import com.aslmk.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DeleteExpiredSessionsJob {
    private final SessionService sessionService;

    @Autowired
    public DeleteExpiredSessionsJob(SessionService sessionService) {
        this.sessionService = sessionService;
    }
    @Scheduled(fixedRate = 14400000) // 4 hours = 240 min = 14400000
    public void deleteExpiredSessions() {
        System.out.println("Deleting expired sessions...");
        sessionService.deleteExpiredSessions(LocalDateTime.now());
    }
}
