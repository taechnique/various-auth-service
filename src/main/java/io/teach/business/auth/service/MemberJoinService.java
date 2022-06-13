package io.teach.business.auth.service;

import io.teach.business.auth.constant.AccountMemberType;
import io.teach.business.auth.constant.AccountProviderType;
import io.teach.business.auth.constant.AccountType;
import io.teach.business.auth.constant.VerifyStatus;
import io.teach.business.auth.dto.AgreementModel;
import io.teach.business.auth.dto.MemberJoinDto;
import io.teach.business.auth.dto.request.ValidateDto;
import io.teach.business.auth.dto.response.MemberAgreement;
import io.teach.business.auth.dto.response.MemberJoinAgreementDto;
import io.teach.business.auth.dto.response.RegisteredResDto;
import io.teach.business.auth.dto.response.RegisteredResultDto;
import io.teach.business.auth.entity.AuthHistory;
import io.teach.business.auth.entity.VerifyInfo;
import io.teach.business.auth.repository.AuthHistoryRepository;
import io.teach.business.auth.repository.UserAuthInfoRepository;
import io.teach.business.auth.repository.VerifyInfoRepository;
import io.teach.business.member.entity.UserAccountInfo;
import io.teach.business.member.entity.UserAuthInfo;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.http.body.DefaultResponse;
import io.teach.infrastructure.http.body.StandardResponse;
import io.teach.infrastructure.util.Util;
import io.teach.infrastructure.util.ValidUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static io.teach.infrastructure.excepted.ServiceStatus.success;
import static io.teach.infrastructure.service.TokenProvider.createYWT;
import static io.teach.infrastructure.service.TokenProvider.encodedBody;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberJoinService {

    private final ValidateService validateService;

    private final AuthHistoryRepository authHistoryRepository;
    private final UserAuthInfoRepository userAuthInfoRepository;
    private final VerifyInfoRepository verifyInfoRepository;

    private final PasswordEncoder passwordEncoder;

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

        checkVerifiedHistory(dto);

        final String nickName = "임의의닉네임10";
        String password = passwordEncoder.encode(dto.getPassword());
        final UserAccountInfo account = UserAccountInfo.create(AccountProviderType.DEFAULT, AccountMemberType.YANOLJA, dto.getEmail(), password, nickName, dto.getPhone());
        final UserAuthInfo authInfo = UserAuthInfo.create(account);

        final UserAuthInfo saved = userAuthInfoRepository.save(authInfo);
        final UserAccountInfo newAccount = saved.getUserAccount();

        final String ywt = createYWT(newAccount.getId(), newAccount.getId(), newAccount.getProviderId(), encodedBody(dto.getEmail(), dto.getPhone()));
        saved.refresh(ywt);

        //== 회원가입 완료 (동의 내용 처리후 쿠폰서비스 이벤트 발행)==//

        return RegisteredResDto.builder()
                .result(success())
                .data(RegisteredResultDto.builder()
                        .agreements(MemberJoinAgreementDto.builder()
                                .location(agreements.getLocation())
                                .marketingPrivacy(agreements.getMarketingPrivacy())
                                .marketingPush(agreements.getMarketingPrivacy())
                                .member(MemberAgreement.builder()
                                        .email(agreements.getPromotion())
                                        .inactivity(agreements.getInactivity())
                                        .sms(agreements.getPromotion())
                                        .build())
                                .privacy(agreements.getPrivacy())
                                .termOfService(agreements.getTermOfService())
                                .build())
                        .appServiceAgreed(agreements.getTermOfService())
                        .hashedMemberNo(newAccount.getHashedMemberNo())
                        .id(newAccount.getEmail())
                        .joinedAtISO8601(newAccount.getCreateTime().toString())
                        .memberNo(newAccount.getId())
                        .memberType(newAccount.getMemberType().getTypeName())
                        .nickName(newAccount.getNickName())
                        .phoneNum(newAccount.getPhone())
                        .phoneNumYN(newAccount.getPhoneYN())
                        .token(ywt)
                        .build())
                .build();
    }

    private void checkVerifiedHistory(final MemberJoinDto dto) {
        validateService.validateJoinField(dto);

        final String email = dto.getEmail();
        final String emailToken = dto.getEmailToken();

        final AuthHistory history = authHistoryRepository.findByVerifiedHistory(emailToken, VerifyStatus.VERIFIED, email)
                .orElseThrow(() -> {
                    log.error("There is no history with verified so that can't check your certificated data.");
                    return new AuthorizingException(ServiceStatus.INVALID_PARAMETER);
                });

        final String phone = dto.getPhone();
        final String certifyCode = dto.getCertifyCode();

        final VerifyInfo verifyInfo = Optional.ofNullable(verifyInfoRepository.findByTarget(phone)).orElseThrow(() -> {
            log.error("There is no verifying target about phone for digits: \"{}\"", phone);
            return new AuthorizingException(ServiceStatus.INVALID_PARAMETER);
        });

        final AuthHistory phoneHistory = verifyInfo.lastHistory().orElseThrow(() -> new AuthorizingException(ServiceStatus.INVALID_PARAMETER));
        phoneHistory.verify(certifyCode);

    }
}
