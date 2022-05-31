package io.teach.business.auth.service;

import io.teach.business.auth.constant.VerifyType;
import io.teach.business.auth.controller.dto.SendEmailDto;
import io.teach.business.auth.entity.AuthHistory;
import io.teach.business.auth.entity.VerifyInfo;
import io.teach.business.auth.repository.AuthHistoryRepository;
import io.teach.business.auth.repository.VerifyInfoRepository;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.http.body.StandardResponse;
import io.teach.infrastructure.properties.VerifyProperties;
import io.teach.infrastructure.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final ValidateService validateService;
    private final VerifyInfoRepository verifyInfoRepository;
    private final AuthHistoryRepository authHistoryRepository;

    private final VerifyProperties verifyProperties;

    @Transactional
    public StandardResponse sendEmailForVerify (final SendEmailDto dto) throws AuthorizingException {
        final String email = dto.getEmail();
        final String group = dto.getGroup();

        //== 이메일 검증 ==//
        validateService.checkDuplicationOfId(email);


        final VerifyInfo found = verifyInfoRepository.findByTarget(email);
        final AuthHistory history = AuthHistory.createHistory(group, VerifyType.EMAIL, verifyProperties.getEmail().getExpiredMinute());

        //== 인증요청 정보가 존재하는경우 (이력 아님) ==//
        if(Util.isNotNull(found)) {

            final List<AuthHistory> sentHistory = found.getAuthHistory().stream()
                    .filter(AuthHistory::wasItSentToday)
                    .collect(Collectors.toList());
            System.out.println("size before send: " + sentHistory.size());

            if(sentHistory.size() < verifyProperties.getEmail().getTodayMax()) {

                found.addAuthHistory(history);
                authHistoryRepository.save(history);

                System.out.println("한개 더 추가");
            } else {
                System.out.println("오늘은 이미 모든 기회를 다썼엉.");
            }

            System.out.println("size after send: " + sentHistory.size());
        } else {
            final VerifyInfo verifyInfo = VerifyInfo.createVerifyInfo(email, VerifyType.EMAIL);
            verifyInfo.addAuthHistory(history);
            authHistoryRepository.save(history);

        }





        return null;
    }
}
