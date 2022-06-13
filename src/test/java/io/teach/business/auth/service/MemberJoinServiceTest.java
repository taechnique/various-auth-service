package io.teach.business.auth.service;

import io.teach.business.auth.constant.HistoryGroup;
import io.teach.business.auth.constant.VerifyStatus;
import io.teach.business.auth.constant.VerifyType;
import io.teach.business.auth.dto.AgreementModel;
import io.teach.business.auth.dto.MemberJoinDto;
import io.teach.business.auth.dto.request.ValidateDto;
import io.teach.business.auth.entity.AuthHistory;
import io.teach.business.auth.entity.VerifyInfo;
import io.teach.business.auth.repository.AuthHistoryRepository;
import io.teach.business.auth.repository.VerifyInfoRepository;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.service.MockingTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class MemberJoinServiceTest extends MockingTester {

    @InjectMocks
    private MemberJoinService memberJoinService;

    @Mock
    private AuthHistoryRepository authHistoryRepository;

    @Mock
    private ValidateService validateService;

    @Mock
    private VerifyInfoRepository verifyInfoRepository;

    @Test
    @DisplayName("[입력값 검증] 존재하지 않는 타입")
    public void validateInJoin1() throws Throwable {
        /* Given */
        final String type = "EXCEL";
        final String value = "test value";

        /* When */
        final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> memberJoinService.validateInJoin(ValidateDto.builder()
                .type(type)
                .value(value)
                .build()));


        /* Then */
        assertEquals(ServiceStatus.INVALID_PARAMETER.getResCode(), actual.getServiceError().getResCode());
    }

    @Test
    @DisplayName("[회원가입] 필수 동의사항이 미동의 인경우")
    public void joinForMember1() throws Throwable {
        /* Given */
        final MemberJoinDto input = MemberJoinDto.builder()
                .agreements(AgreementModel.builder()
                        .termOfService(true)
                        .privacy(false)
                        .build())
                .build();

        /* When */
        final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> memberJoinService.joinForMember(input));

        /* Then */
        assertEquals(ServiceStatus.NEED_ESSENTIAL_AGREEMENT.getResCode(), actual.getServiceError().getResCode());
    }

    @Test
    @DisplayName("[회원가입] 인증된 이력이 미존재")
    public void joinForMember2() throws Throwable {
        /* Given */
        final MemberJoinDto input = MemberJoinDto.builder()
                .agreements(AgreementModel.builder()
                        .termOfService(true)
                        .privacy(true)
                        .build())
                .email("taechnique@yanolja.com")
                .emailToken("c6ece304-22da-4fbd-bc3a-96316c562cad")
                .password("yanolja4ever!")
                .passwordConfirm("yanolja4ever!")
                .build();

        /* When */
        when(authHistoryRepository.findByVerifiedHistory(input.getEmailToken(), VerifyStatus.VERIFIED, input.getEmail()))
                .thenReturn(Optional.ofNullable(null));

        /* Then */
        final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> memberJoinService.joinForMember(input));
        assertEquals(ServiceStatus.INVALID_PARAMETER.getResCode(), actual.getServiceError().getResCode());

    }

    @Test
    @DisplayName("[회원가입] 휴대폰 인증 요청 정보가 미존재")
    public void joinForMember3() throws Throwable {
        /* Given */
        final MemberJoinDto input = MemberJoinDto.builder()
                .agreements(AgreementModel.builder()
                        .termOfService(true)
                        .privacy(true)
                        .build())
                .email("taechnique@yanolja.com")
                .emailToken("c6ece304-22da-4fbd-bc3a-96316c562cad")
                .password("yanolja4ever!")
                .passwordConfirm("yanolja4ever!")
                .phone("0101010101")
                .build();
        final AuthHistory history = AuthHistory.createHistory(HistoryGroup.JOIN, VerifyType.EMAIL, 600);

        /* When */
        when(authHistoryRepository.findByVerifiedHistory(input.getEmailToken(), VerifyStatus.VERIFIED, input.getEmail()))
                .thenReturn(Optional.ofNullable(history));
        when(verifyInfoRepository.findByTarget(input.getPhone()))
                .thenReturn(null);

        /* Then */
        final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> memberJoinService.joinForMember(input));
        assertEquals(ServiceStatus.INVALID_PARAMETER.getResCode(), actual.getServiceError().getResCode());

    }

}