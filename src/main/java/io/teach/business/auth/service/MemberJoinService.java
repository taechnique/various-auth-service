package io.teach.business.auth.service;

import io.teach.business.auth.constant.AccountType;
import io.teach.business.auth.dto.AgreementModel;
import io.teach.business.auth.dto.MemberJoinDto;
import io.teach.business.auth.dto.request.ValidateDto;
import io.teach.business.auth.dto.response.ValidationResDto;
import io.teach.business.auth.entity.AuthHistory;
import io.teach.business.auth.repository.AuthHistoryRepository;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.http.body.DefaultResponse;
import io.teach.infrastructure.http.body.StandardResponse;
import io.teach.infrastructure.util.Util;
import io.teach.infrastructure.util.ValidUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static io.teach.infrastructure.excepted.ServiceStatus.success;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberJoinService {

    private final ValidateService validateService;
    private final AuthHistoryRepository authHistoryRepository;

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
            case PASSWORD: {
                validateService.validatePassword(value);
                break;
            }
            default: {
                log.info("Not found validate service for \"{}\"", type);
                throw new AuthorizingException(ServiceStatus.INVALID_PARAMETER);
            }
        }

        return DefaultResponse.ok();
    }

    @Transactional
    public StandardResponse joinForMember(final MemberJoinDto dto) throws AuthorizingException {
        final AgreementModel agreements = dto.getAgreements();

        ValidUtil.checkEssentialEntries(agreements.getTermOfService(), agreements.getPrivacy())
                .ifPresent((b) -> {
                    throw new AuthorizingException(ServiceStatus.NEED_ESSENTIAL_AGREEMENT);
                });

        checkVerifyHistoryAndCreateAccount(dto);

        return null;
    }

    private void checkVerifyHistoryAndCreateAccount(final MemberJoinDto dto) {
        validateService.validateJoinField(dto);

        final String email = dto.getEmail();
        final String emailToken = dto.getEmailToken();
        final String certifyCode = dto.getCertifyCode();

        final AuthHistory history = authHistoryRepository.findByVerifiedHistory(emailToken, certifyCode, email)
                .orElseThrow(() -> {
                    log.error("");
                    return new AuthorizingException(ServiceStatus.INVALID_PARAMETER);
                });


    }
}
