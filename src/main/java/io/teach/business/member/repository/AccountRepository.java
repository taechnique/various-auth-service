package io.teach.business.member.repository;

import io.teach.business.member.entity.UserAccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<UserAccountInfo, Long> {

    @Query("select ac from UserAccountInfo ac where ac.loginId = ?1")
    UserAccountInfo findLoginId(final String loginId);
}
