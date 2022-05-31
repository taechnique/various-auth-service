package io.teach.business.auth.repository;

import io.teach.business.auth.entity.VerifyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VerifyInfoRepository extends JpaRepository<VerifyInfo, Long> {

    @Query("select vi from VerifyInfo vi where vi.verifyTarget = ?1")
    VerifyInfo findByTarget(String target);
}
