package io.teach.business.auth.repository;

import io.teach.business.auth.constant.VerifyStatus;
import io.teach.business.auth.entity.AuthHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthHistoryRepository extends JpaRepository<AuthHistory, Long> {

    @Query("select " +
            "ah " +
            "from AuthHistory ah " +
            "where " +
            "ah.expiredTime > current_time " +
            "and ah.verifyPermitToken = ?1 ")
    AuthHistory findByToken(final String token);

    @Query("select " +
            "ah " +
            "from AuthHistory ah " +
            "left outer join VerifyInfo vi " +
            "on ah.verifyInfo.id = vi.id " +
            "where " +
            "ah.verifyPermitToken = ?1 " +
            "and ah.sendTime is not null " +
            "and vi.verifyNumber = ?2 " +
            "and vi.verifyTarget = ?3 " +
            "and ah.verifyStatus = ?4")

    Optional<AuthHistory> findByVerifiedHistory(final String token, final String verifyCode, final String target, final VerifyStatus status);
}
