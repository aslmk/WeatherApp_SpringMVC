package com.aslmk.scheduler;

import com.aslmk.service.SessionService;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class DeleteExpiredSessionsJob {
    private static final Logger log = LoggerFactory.getLogger(DeleteExpiredSessionsJob.class);
    private final SessionService sessionService;

    @Autowired
    public DeleteExpiredSessionsJob(SessionService sessionService) {
        this.sessionService = sessionService;
    }
    @Scheduled(fixedRate = 14400000) // 4 hours = 240 min = 14400000
    public void deleteExpiredSessions() {
        log.info("Deleting expired sessions");
        sessionService.deleteExpiredSessions(LocalDateTime.now());
    }
}
