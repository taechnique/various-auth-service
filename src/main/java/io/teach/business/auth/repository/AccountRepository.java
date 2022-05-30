package io.teach.business.auth.repository;

import io.teach.business.auth.entity.UserAccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<UserAccountInfo, Long> {

    @Query("select ac from UserAccountInfo ac where ac.email = ?1")
    UserAccountInfo findByEmail(String email);
}