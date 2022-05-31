package io.teach.business.auth.service;

import io.teach.business.auth.dto.ValidationResDto;
import io.teach.business.auth.entity.UserAccountInfo;
import io.teach.business.auth.repository.AccountRepository;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.http.body.StandardResponse;
import io.teach.infrastructure.util.Util;
import io.teach.infrastructure.util.ValidUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static io.teach.infrastructure.excepted.ServiceStatus.success;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidateService {

    private final AccountRepository accountRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public void checkDuplicationOfId(final String value) throws AuthorizingException {
        if( ! ValidUtil.email(value)) {
            log.error("Invalid email format. Cannot use this email 'cause invalid form: \"{}\"", value);
            throw new AuthorizingException(ServiceStatus.INVALID_PARAMETER);
        }

        final UserAccountInfo found = accountRepository.findLoginId(value);

        if(Util.isNotNull(found)) {
            log.info("Already exist loginId with \"{}\"", value);
            throw new AuthorizingException(ServiceStatus.ALREADY_EXIST_LOGIN_ID);
        }

        log.info("Successfully check for duplication of login id: \"{}\"", value);
    }
}
