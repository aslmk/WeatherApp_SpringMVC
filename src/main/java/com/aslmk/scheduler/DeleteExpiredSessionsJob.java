package com.aslmk.scheduler;

import com.aslmk.model.Sessions;
import com.aslmk.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DeleteExpiredSessionsJob {
    private SessionService sessionService;

    @Autowired
    public DeleteExpiredSessionsJob(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Scheduled(fixedRate = 900000 ) // 15 min = 900000
    public void deleteExpiredSessions() {
        System.out.println("Deleting expired sessions...");
        sessionService.deleteExpiredSessions(LocalDateTime.now());
        System.out.println("Expired sessions deleted.");

    }
}
