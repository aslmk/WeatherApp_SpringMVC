package com.aslmk.repository;

import com.aslmk.model.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Sessions, String> {
}
