package io.teach.business.auth.service;

import io.teach.business.auth.dto.AuthResponseDto;
import io.teach.business.auth.entity.UserAccountInfo;
import io.teach.business.auth.repository.AccountRepository;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.http.body.StandardResponse;
import io.teach.infrastructure.service.MailService;
import io.teach.infrastructure.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonAuthService {

    private final AccountRepository accountRepo;
    private final MailService mailService;

    @Transactional(readOnly = true)
    public StandardResponse checkEmailAndSend(final String type, final String info) throws AuthorizingException {
        final String emailRegex = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

        if (!Pattern.compile(emailRegex).matcher(info).matches())
            throw new AuthorizingException(ServiceStatus.INVALID_EMAIL_FORM);

        return checkEmail(info);
    }

    private StandardResponse checkEmail(final String email) {

        final UserAccountInfo found = accountRepo.findByEmail(email);
        if(Util.isNotNull(found)) {
            log.info("There is already existed user with email: \"{}\"", email);
            throw new AuthorizingException(ServiceStatus.ALREADY_EXIST_EMAIL);
        }

        //== 인증 이메일 발송 ==//

        return AuthResponseDto.builder()
                .resCode(ServiceStatus.SUCCESS.getResCode())
                .usable(email)
                .build();
    }

    private StandardResponse checkPhone(final String info) {
        return null;
    }
}
