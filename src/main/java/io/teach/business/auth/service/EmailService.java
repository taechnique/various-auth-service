package io.teach.business.auth.service;

import io.teach.business.auth.constant.VerifyType;
import io.teach.business.auth.controller.dto.SendEmailDto;
import io.teach.business.auth.entity.AuthHistory;
import io.teach.business.auth.entity.VerifyInfo;
import io.teach.business.auth.repository.VerifyInfoRepository;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.http.body.StandardResponse;
import io.teach.infrastructure.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final ValidateService validateService;
    private final VerifyInfoRepository verifyInfoRepository;

    @Transactional
    public StandardResponse sendEmailForVerify (final SendEmailDto dto) throws AuthorizingException {
        final String email = dto.getEmail();
        final String group = dto.getGroup();

        //== 이메일 검증 ==//
        validateService.checkDuplicationOfId(email);


        final VerifyInfo found = verifyInfoRepository.findByTarget(email);

        //== 인증요청 정보가 존재하는경우 ==//
        if(Util.isNotNull(found)) {

            found.getAuthHistory().stream().filter(his -> his.getSendTime())
        }


        final AuthHistory history = AuthHistory.createHistory(group, VerifyType.EMAIL);



        return null;
    }
}
