package io.teach.business.auth.service;

import io.teach.business.auth.constant.VerifyType;
import io.teach.business.auth.controller.dto.SendEmailDto;
import io.teach.business.auth.entity.AuthHistory;
import io.teach.business.auth.entity.VerifyInfo;
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
    public void sendEmailForVerify2() throws Throwable {
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
}