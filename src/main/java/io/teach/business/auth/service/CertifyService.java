package io.teach.business.auth.service;

import io.teach.business.auth.constant.HistoryGroup;
import io.teach.business.auth.constant.VerifyStatus;
import io.teach.business.auth.constant.VerifyType;
import io.teach.business.auth.dto.SendEmailDto;
import io.teach.business.auth.dto.SendMessageResDto;
import io.teach.business.auth.dto.SendPhoneDto;
import io.teach.business.auth.dto.request.ConfirmEmailDto;
import io.teach.business.auth.dto.response.CountModel;
import io.teach.business.auth.dto.response.SendEmailResDto;
import io.teach.business.auth.dto.response.SendEmailResultDto;
import io.teach.business.auth.entity.AuthHistory;
import io.teach.business.auth.entity.VerifyInfo;
import io.teach.business.auth.repository.AuthHistoryRepository;
import io.teach.business.auth.repository.VerifyInfoRepository;
import io.teach.infrastructure.constant.ServiceMent;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.http.body.DefaultResponse;
import io.teach.infrastructure.http.body.StandardResponse;
import io.teach.infrastructure.properties.VerifyProperties;
import io.teach.infrastructure.service.AsyncTransferService;
import io.teach.infrastructure.util.Util;
import io.teach.infrastructure.util.ValidUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Supplier;

import static io.teach.infrastructure.excepted.ServiceStatus.success;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertifyService {

    private final ValidateService validateService;
    private final VerifyInfoRepository verifyInfoRepository;
    private final AuthHistoryRepository authHistoryRepository;

    private final VerifyProperties verifyProperties;
    private final AsyncTransferService asyncEmailService;


    @Transactional
    public StandardResponse sendEmailForVerify (final SendEmailDto dto) throws AuthorizingException {
        final String email = dto.getEmail();
        final String group = dto.getGroup();

        if( ! ValidUtil.email(email)) {
            log.error("Invalid email format. Please check email. \"{}\"", email);
            throw new AuthorizingException(ServiceStatus.INVALID_PARAMETER);
        }

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

    @Transactional(propagation = Propagation.NESTED)
    public StandardResponse confirmEmailForVerify(final ConfirmEmailDto reqDto) throws AuthorizingException {
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
    

    @Transactional
    public StandardResponse sendMessageForCertify(final SendPhoneDto reqDto) throws AuthorizingException {
        final String group = reqDto.getGroup();
        final String phoneNum = ValidUtil.phone(reqDto.getPhoneNum(), false);

        final Integer maxToday = verifyProperties.getPhonePolicy().getTodayMax();
        final Integer expiredSecond = verifyProperties.getPhonePolicy().getExpiredSecond();
        final Integer codeLength = verifyProperties.getPhonePolicy().getCodeLength();

        final HistoryGroup historyGroup = HistoryGroup.groupOf(group).orElseThrow(() -> {
            log.error("올바르지 않은 히스토리 그룹(\"{}\")입니다.", group);
            return new AuthorizingException(ServiceStatus.INVALID_PARAMETER);
        });

        if(Util.isNull(phoneNum)) {
            log.error("올바르지않은 휴대폰 형식입니다. 다시 확인해주세요. (\"{}\")", reqDto.getPhoneNum());
            throw new AuthorizingException(ServiceStatus.INVALID_PARAMETER);
        }


        final VerifyInfo found = verifyInfoRepository.findByTarget(phoneNum);
        final AuthHistory history = AuthHistory.createHistory(historyGroup, VerifyType.PHONE, expiredSecond);

        //==인증 요청 미존재 ==//
        if (Util.isNull(found)) {

            VerifyInfo.createVerifyInfo(phoneNum, VerifyType.PHONE, codeLength, history);
            authHistoryRepository.save(history);
        } else {
            if (found.getTodayCount() < maxToday) {

                found.cancelOldHistories();
                found.refreshVerifyToken(history, codeLength);
                authHistoryRepository.save(history);
            } else
                throw new AuthorizingException(ServiceStatus.ALREADY_SPENT_ALL_PHONE_CHANCE);

        }

        final VerifyInfo info = history.getVerifyInfo();
        final Integer remain = info.getTodayRemainCount(maxToday);
        final Integer use = info.getTodayCount();

        //== SMS 전송 서비스 ==//
        asyncEmailService.sendMessageAsAsync(phoneNum, info.getVerifyNumber());

        return SendMessageResDto.builder()
                .count(CountModel.left(remain, use))
                .expired(expiredSecond)
                .message(String.format(ServiceMent.SEND_VERIFY_NUMBER.form(), remain))
                .result(success())
                .build();
    }

}
