package io.teach.business.auth.service;

import io.teach.business.auth.constant.HistoryGroup;
import io.teach.business.auth.constant.VerifyType;
import io.teach.business.auth.controller.dto.SendEmailDto;
import io.teach.business.auth.dto.request.ConfirmEmailDto;
import io.teach.business.auth.dto.response.CountModel;
import io.teach.business.auth.dto.response.SendEmailResDto;
import io.teach.business.auth.dto.response.SendEmailResultDto;
import io.teach.business.auth.entity.AuthHistory;
import io.teach.business.auth.entity.VerifyInfo;
import io.teach.business.auth.repository.AuthHistoryRepository;
import io.teach.business.auth.repository.VerifyInfoRepository;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.http.body.DefaultResponse;
import io.teach.infrastructure.http.body.StandardResponse;
import io.teach.infrastructure.properties.VerifyProperties;
import io.teach.infrastructure.service.AsyncEmailTransferService;
import io.teach.infrastructure.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static io.teach.infrastructure.excepted.ServiceStatus.success;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final ValidateService validateService;
    private final VerifyInfoRepository verifyInfoRepository;
    private final AuthHistoryRepository authHistoryRepository;

    private final VerifyProperties verifyProperties;
    private final AsyncEmailTransferService asyncEmailService;


    @Transactional
    public StandardResponse sendEmailForVerify (final SendEmailDto dto) throws AuthorizingException {
        final String email = dto.getEmail();
        final String group = dto.getGroup();
        final Integer expiredSecond = verifyProperties.getEmailPolicy().getExpiredSecond();
        final Integer maxToday = verifyProperties.getEmailPolicy().getTodayMax();
        final Integer codeLength = verifyProperties.getEmailPolicy().getCodeLength();

        final HistoryGroup historyGroup = HistoryGroup.groupOf(group).orElseThrow(() -> {
            log.error("Invalid history group.");
            return new AuthorizingException(ServiceStatus.INVALID_PARAMETER);
        });


        //== 이메일 검증 ==//
        validateService.checkDuplicationOfId(email);

        final VerifyInfo found = verifyInfoRepository.findByTarget(email);
        final AuthHistory history = AuthHistory.createHistory(historyGroup, VerifyType.EMAIL, expiredSecond);

        //==인증 요청 미존재 ==//
        if(Util.isNull(found)) {

            VerifyInfo.createVerifyInfo(email, VerifyType.EMAIL, codeLength, history);
            authHistoryRepository.save(history);
        } else {
            if (found.getTodayCount() < maxToday) {

                found.cancelOldHistories();
                found.refreshVerifyToken(history, codeLength);
                authHistoryRepository.save(history);
            } else
                throw new AuthorizingException(ServiceStatus.ALREADY_SPENT_ALL_EMAIL_CHANCE);

        }

        final Integer remain = history.getVerifyInfo().getTodayRemainCount(maxToday);

        //== 이메일 전송 서비스==//
        asyncEmailService.sendEmailAsAsync(email, history.getVerifyInfo().getVerifyNumber());


        return SendEmailResDto.builder()
                .result(success())
                .data(SendEmailResultDto.make(codeLength, expiredSecond, history.getVerifyPermitToken(), CountModel.left(remain)))
                .build();
    }
    @Transactional
    public StandardResponse confirmEmailForVerify(final ConfirmEmailDto reqDto) {
        final String code = reqDto.getCode();
        final String token = reqDto.getToken();

        final AuthHistory found = Optional.ofNullable(authHistoryRepository.findByToken(token))
                .orElseThrow(() -> {
                    log.info("토큰 [{}]의 인증 요청 이력이 존재하지 않습니다.", token);
                    return new AuthorizingException(ServiceStatus.INVALID_PARAMETER);
                });

        found.verify(code);

        return DefaultResponse.ok();
    }
}
