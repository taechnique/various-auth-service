package io.teach.business.auth.service;

import io.teach.business.auth.dto.MemberJoinDto;
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

    @Test
    @DisplayName("[회원가입 입력정보 유효성 검사] 올바른 입력값")
    public void validateJoinField() throws Throwable {
        /* Given */
        final MemberJoinDto input = MemberJoinDto.builder()
                .email("taechnique@yanolja.com")
                .emailToken("4ed179f6-9703-4a64-b7c3-b27ca92ff0d1")
                .password("yanolja4ever!")
                .passwordConfirm("yanolja4ever!")
                .phone("010-101-0101")
                .build();

        /* When */
        validateService.validateJoinField(input);

        /* Then */
        assertEquals("0101010101", input.getPhone());

    }

    @Test
    @DisplayName("[회원가입 입력정보 유효성 검사] 올바르지 않은 이메일 형식")
    public void validateJoinField1() throws Throwable {
        /* Given */
        final MemberJoinDto input = MemberJoinDto.builder()
                .email("taechnique@ds")
                .emailToken("4ed179f6-9703-4a64-b7c3-b27ca92ff0d1")
                .password("yanolja4ever!")
                .passwordConfirm("yanolja4ever!")
                .build();

        /* When */
        final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> validateService.validateJoinField(input));

        /* Then */
        assertEquals(ServiceStatus.INVALID_PARAMETER.getResCode(), actual.getServiceError().getResCode());

    }

    @Test
    @DisplayName("[회원가입 입력정보 유효성 검사] 올바르지 않은 토큰 형식")
    public void validateJoinField2() throws Throwable {
        /* Given */
        final MemberJoinDto input = MemberJoinDto.builder()
                .email("taechnique@yanolja.com")
                .emailToken("4ed179f-9703-4a64-b7c3-b27ca92ff0d1")
                .password("yanolja4ever!")
                .passwordConfirm("yanolja4ever!")
                .build();

        /* When */
        final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> validateService.validateJoinField(input));

        /* Then */
        assertEquals(ServiceStatus.INVALID_PARAMETER.getResCode(), actual.getServiceError().getResCode());

    }

    @Test
    @DisplayName("[회원가입 입력정보 유효성 검사] 일치하지않는 비밀번호 페어")
    public void validateJoinField3() throws Throwable {
        /* Given */
        final MemberJoinDto input = MemberJoinDto.builder()
                .email("taechnique@yanolja.com")
                .emailToken("4ed17a9f-9703-4a64-b7c3-b27ca92ff0d1")
                .password("yanolja4ever!")
                .passwordConfirm("yanolja4ever@")
                .build();

        /* When */
        final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> validateService.validateJoinField(input));

        /* Then */
        assertEquals(ServiceStatus.INVALID_PARAMETER.getResCode(), actual.getServiceError().getResCode());
    }

    @Test
    @DisplayName("[회원가입 입력정보 유효성 검사] 올바르지 않은 휴대폰 번호 형식 (하이픈 필수)")
   public void validateJoinField4() throws Throwable {
        /* Given */
        final MemberJoinDto input = MemberJoinDto.builder()
                .email("taechnique@yanolja.com")
                .emailToken("4ed17a9f-9703-4a64-b7c3-b27ca92ff0d1")
                .password("yanolja4ever!")
                .passwordConfirm("yanolja4ever!")
                .phone("010-1010101")
                .build();

        /* When */
        final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> validateService.validateJoinField(input));

        /* Then */
        assertEquals(ServiceStatus.INVALID_PARAMETER.getResCode(), actual.getServiceError().getResCode());

    }
}