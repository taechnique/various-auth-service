package io.teach.business.auth.repository;

import io.teach.business.auth.constant.VerifyType;
import io.teach.business.auth.entity.AuthHistory;
import io.teach.business.auth.entity.VerifyInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class AuthHistoryRepositoryTest {

    @Autowired
    private AuthHistoryRepository repository;

    @Test
    @DisplayName("[토큰으로 요청이력 조회] 정상")
    public void findByToken1() throws Throwable {
        /* Given */
        final VerifyType type = VerifyType.EMAIL;
        final AuthHistory history = AuthHistory.createHistory("JOIN", type, 600);
        VerifyInfo.createVerifyInfo("taechnique@yanalja.com", type, 6, history);


        /* When */
        repository.save(history);
        final AuthHistory found = repository.findByToken(history.getVerifyPermitToken());

        /* Then */
        assertNotNull(found);
        assertEquals(history.getVerifyPermitToken(), found.getVerifyPermitToken());
    }
}