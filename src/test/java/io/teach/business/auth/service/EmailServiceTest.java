package io.teach.business.auth.service;

import io.teach.business.auth.constant.VerifyType;
import io.teach.business.auth.controller.dto.SendEmailDto;
import io.teach.business.auth.dto.request.ConfirmEmailDto;
import io.teach.business.auth.entity.AuthHistory;
import io.teach.business.auth.entity.VerifyInfo;
import io.teach.business.auth.repository.AuthHistoryRepository;
import io.teach.business.auth.repository.VerifyInfoRepository;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.properties.VerifyProperties;
import io.teach.infrastructure.properties.VerifyProperties.EmailPolicy;
import io.teach.infrastructure.service.MockingTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@Transactional
class EmailServiceTest extends MockingTester {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private VerifyProperties verifyProperties;

    @Mock
    private ValidateService validateService;

    @Mock
    private VerifyInfoRepository verifyInfoRepository;

    @Mock
    private AuthHistoryRepository authHistoryRepository;

    @BeforeEach
    public void init() {

        EmailPolicy policy = new EmailPolicy();
        policy.setCodeLength(6);
        policy.setExpiredSecond(600);
        policy.setTodayMax(5);

        lenient().when(verifyProperties.getEmailPolicy())
                .thenReturn(policy);
    }

    @Test
    @DisplayName("[인증요청 이메일 전송] 하루 최대 요청가능한 횟수가 넘은 경우")
    public void sendEmailForVerify1() throws Throwable {
        /* Given */
        String id = "test@example.com";
        String group = "JOIN";
        SendEmailDto input = SendEmailDto.builder().email(id).group(group).build();

        VerifyType type = VerifyType.EMAIL;
        Integer expiredSecond = verifyProperties.getEmailPolicy().getExpiredSecond();
        Integer codeLength = verifyProperties.getEmailPolicy().getCodeLength();

        final AuthHistory history = AuthHistory.createHistory(group, type, expiredSecond);
        final VerifyInfo info = VerifyInfo.createVerifyInfo(id, type, codeLength, history);

        Arrays.asList(
                AuthHistory.createHistory(group, type, expiredSecond),
                AuthHistory.createHistory(group, type, expiredSecond),
                AuthHistory.createHistory(group, type, expiredSecond),
                AuthHistory.createHistory(group, type, expiredSecond)
        ).stream().forEach(his -> info.refreshVerifyToken(his, codeLength));

        /* When */
        when(verifyInfoRepository.findByTarget(id))
                .thenReturn(info);
        AuthorizingException actual = assertThrows(AuthorizingException.class, () -> emailService.sendEmailForVerify(input));

        /* Then */
        assertEquals(ServiceStatus.ALREADY_SPENT_ALL_EMAIL_CHANCE, actual.getServiceError());

    }

    @Test
    @DisplayName("[이메일 인증번호 확인] 요청 이력이 존재하지 않는경우")
    public void confirmEmailForVerify1() throws Throwable {
        /* Given */
        final ConfirmEmailDto input = ConfirmEmailDto.builder()
                .code("414233")
                .token("8512d9a0-4122-4fc8-a3ba-3c34b42b334e")
                .build();

        /* When */
        when(authHistoryRepository.findByToken(input.getToken()))
                .thenReturn(null);
        final AuthorizingException actual = assertThrows(AuthorizingException.class, () ->
                emailService.confirmEmailForVerify(input));

        /* Then */
        assertEquals(ServiceStatus.INVALID_PARAMETER, actual.getServiceError());
    }
}