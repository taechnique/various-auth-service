package io.teach.business.member.repository;

import io.teach.business.member.entity.UserAuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<UserAuthInfo, Long> {

}
