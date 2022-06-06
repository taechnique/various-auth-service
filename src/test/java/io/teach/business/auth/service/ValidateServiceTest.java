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
        final String id = "taechnique@yanolja.com";
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

    @Test
    @DisplayName("[비밀번호 유효성 검사] 올바른 비밀번호 형식")
    public void validatePassword() throws Throwable {
        /* Given */
        final String password = "yanolja4ever!";

        /* When */
        assertDoesNotThrow(() -> validateService.validatePassword(password));

        /* Then */

    }

    @Test
    @DisplayName("[비밀번호 유효성 검사] 올바르지 않은 비밀번호 형식 - 숫자가 없는경우")
    public void validatePassword1() throws Throwable {
        /* Given */
        final String password = "onlyYanolja!";

        /* When */
        AuthorizingException actual = assertThrows(AuthorizingException.class, () -> validateService.validatePassword(password));

        /* Then */
        assertEquals(ServiceStatus.INVALID_PASSWORD_FORMAT, actual.getServiceError());
    }

    @Test
    @DisplayName("[비밀번호 유효성 검사] 올바르지 않은 비밀번호 형식 - 영어가 없는경우")
    public void validatePassword2() throws Throwable {
        /* Given */
        final String password = "3214@321!";

        /* When */
        AuthorizingException actual = assertThrows(AuthorizingException.class, () -> validateService.validatePassword(password));

        /* Then */
        assertEquals(ServiceStatus.INVALID_PASSWORD_FORMAT, actual.getServiceError());
    }

    @Test
    @DisplayName("[비밀번호 유효성 검사] 올바르지 않은 비밀번호 형식 - 특수문자가 없는경우")
    public void validatePassword3() throws Throwable {
        /* Given */
        final String password = "onlyYanolja2";

        /* When */
        AuthorizingException actual = assertThrows(AuthorizingException.class, () -> validateService.validatePassword(password));

        /* Then */
        assertEquals(ServiceStatus.INVALID_PASSWORD_FORMAT, actual.getServiceError());
    }

    @Test
    @DisplayName("[비밀번호 유효성 검사] 올바르지 않은 비밀번호 형식 - 비허용 길이")
    public void validatePassword4() throws Throwable {
        /* Given */
        final String password1 = "onlyYa!"; // = 7
        final String password2 = "onlyYdishfwkfhhd1saa!"; // = 21

        /* When */
        AuthorizingException actual1 = assertThrows(AuthorizingException.class, () -> validateService.validatePassword(password1));
        AuthorizingException actual2 = assertThrows(AuthorizingException.class, () -> validateService.validatePassword(password1));

        /* Then */
        assertEquals(ServiceStatus.INVALID_PASSWORD_FORMAT, actual1.getServiceError());
        assertEquals(ServiceStatus.INVALID_PASSWORD_FORMAT, actual2.getServiceError());
    }
}