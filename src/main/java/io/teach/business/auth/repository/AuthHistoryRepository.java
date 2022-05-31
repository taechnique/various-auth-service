package io.teach.business.auth.repository;

import io.teach.business.auth.entity.AuthHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthHistoryRepository extends JpaRepository<AuthHistory, Long> {
}
