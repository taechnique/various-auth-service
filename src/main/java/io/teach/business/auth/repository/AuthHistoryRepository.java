package io.teach.business.auth.repository;

import io.teach.business.auth.entity.AuthHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface AuthHistoryRepository extends JpaRepository<AuthHistory, Long> {

    @Query("select " +
            "ah " +
            "from AuthHistory ah " +
            "where " +
            "ah.expiredTime > current_time " +
            "and ah.verifyPermitToken = ?1 ")
    AuthHistory findByToken(String token);

    
}
