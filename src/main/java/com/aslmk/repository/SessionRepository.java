package com.aslmk.repository;

import com.aslmk.model.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface SessionRepository extends JpaRepository<Sessions, String> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Sessions s WHERE s.expiresat <= ?1", nativeQuery = true)
    void deleteExpiredSessions(LocalDateTime time);
}
