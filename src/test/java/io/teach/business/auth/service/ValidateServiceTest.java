package io.teach.business.auth.service;

import io.teach.business.member.entity.UserAccountInfo;
import io.teach.business.member.repository.AccountRepository;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.service.MockingTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Transactional
class ValidateServiceTest extends MockingTester {

    @InjectMocks
    private ValidateService validateService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    @DisplayName("[이메일 중복검사] 잘못된 이메일 형식")
    public void checkDuplicationOfId1() throws Throwable {
        /* Given */
        final String email = "test@email";

        /* When */
        final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> validateService.checkDuplicationOfId(email));

        /* Then */
        assertEquals(ServiceStatus.INVALID_PARAMETER, actual.getServiceError());
    }

    @Test
    @DisplayName("[이메일 중복검사] 이미 존재하는 계정")
    public void checkDuplicationOfId2() throws Throwable {
        /* Given */
        final String id = "test@email.com";
        final String passphrase = "njd1!ln@sa";
        final UserAccountInfo found = UserAccountInfo.create(id, passphrase);

        /* When */
        when(accountRepository.findLoginId(id))
                .thenReturn(found);
        final AuthorizingException actual = assertThrows(AuthorizingException.class, () ->
                validateService.checkDuplicationOfId(id));

        /* Then */
        assertEquals(ServiceStatus.ALREADY_EXIST_LOGIN_ID, actual.getServiceError());
    }
}