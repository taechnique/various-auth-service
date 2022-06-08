package io.teach.business.auth.service;

import io.teach.business.auth.dto.MemberJoinDto;
import io.teach.business.member.entity.UserAccountInfo;
import io.teach.business.member.repository.AccountRepository;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.util.Util;
import io.teach.infrastructure.util.ValidUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
            log.error("Already exist loginId with \"{}\"", value);
            throw new AuthorizingException(ServiceStatus.ALREADY_EXIST_LOGIN_ID);
        }

        log.info("Successfully check for duplication of login id: \"{}\"", value);
    }

    public void validatePassword(final String value) throws AuthorizingException {
        if( ! ValidUtil.password(value))
            throw new AuthorizingException(ServiceStatus.INVALID_PASSWORD_FORMAT);

    }

    public void validateJoinField(final MemberJoinDto dto) throws AuthorizingException {
        if(
                ValidUtil.email(dto.getEmail()) &&
                ValidUtil.uuid(dto.getEmailToken()) &&
                ValidUtil.password(dto.getPassword(), dto.getPasswordConfirm())) {

            dto.setPhone(Optional.ofNullable(ValidUtil.phone(dto.getPhone(), false)).orElseThrow(() -> {
                log.error("Invalid  digits form with phone. check this digits: \"{}\"", dto.getPhone());
                throw new AuthorizingException(ServiceStatus.INVALID_PARAMETER);
            }));
            return;
        }

        log.error("There is problem with validating for input object.");
        throw new AuthorizingException(ServiceStatus.INVALID_PARAMETER);

    }
}
