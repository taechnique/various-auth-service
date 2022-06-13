package io.teach.business.auth.repository;

import io.teach.business.member.entity.UserAuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthInfoRepository extends JpaRepository<UserAuthInfo, Long> {
}
