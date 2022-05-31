package io.teach.business.auth.service;

import io.teach.business.auth.constant.AccountType;
import io.teach.business.auth.dto.ValidateDto;
import io.teach.business.auth.dto.ValidationResDto;
import io.teach.business.auth.repository.AccountRepository;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.http.body.StandardResponse;
import io.teach.infrastructure.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.teach.infrastructure.excepted.ServiceStatus.success;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberJoinService {

    private final ValidateService validateService;

    @Transactional(readOnly = true)
    public StandardResponse validateInJoin(final ValidateDto dto) throws AuthorizingException {

        final String type = dto.getType();
        final String value = dto.getValue();
        final AccountType accountType = AccountType.typeOf(type);

        if(Util.isNull(accountType))
            throw new AuthorizingException(ServiceStatus.INVALID_PARAMETER);


        switch (accountType) {
            case ID: {
                validateService.checkDuplicationOfId(value);
                break;
            }
            default: {
                log.info("Not found validate service for \"{}\"", type);
                throw new AuthorizingException(ServiceStatus.INVALID_PARAMETER);
            }
        }

        return ValidationResDto.builder()
                .result(success())
                .build();
    }
}
