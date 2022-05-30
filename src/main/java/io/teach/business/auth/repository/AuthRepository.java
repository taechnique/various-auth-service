package io.teach.business.auth.repository;

import io.teach.business.auth.entity.UserAuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<UserAuthInfo, Long> {

}
