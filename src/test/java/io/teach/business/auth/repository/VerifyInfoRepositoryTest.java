package io.teach.business.auth.repository;

import io.teach.business.auth.constant.HistoryGroup;
import io.teach.business.auth.constant.VerifyType;
import io.teach.business.auth.entity.AuthHistory;
import io.teach.business.auth.entity.VerifyInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class VerifyInfoRepositoryTest {

    @Autowired
    private AuthHistoryRepository authHistoryRepository;

    @Autowired
    private VerifyInfoRepository verifyInfoRepository;

    @Test
    @DisplayName("인증 요청 대상으로 조회 (이메일)")
    public void findByTarget() throws Throwable {
        /* Given */
        final String email = "test@test.com";
        VerifyType verifyType = VerifyType.EMAIL;

        final AuthHistory history = AuthHistory.createHistory(HistoryGroup.JOIN, verifyType, 600);
        VerifyInfo.createVerifyInfo(email, verifyType, 6, history);

        /* When */
        authHistoryRepository.save(history);

        /* Then */
        assertEquals(email, history.getVerifyInfo().getVerifyTarget());

    }
}