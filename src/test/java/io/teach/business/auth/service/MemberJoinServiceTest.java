package io.teach.business.auth.service;

import io.teach.business.auth.dto.AgreementModel;
import io.teach.business.auth.dto.MemberJoinDto;
import io.teach.business.auth.dto.request.ValidateDto;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.service.MockingTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberJoinServiceTest extends MockingTester {

    @InjectMocks
    private MemberJoinService memberJoinService;

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
    @DisplayName("[입력값 검증] 필수 동의사항이 미동의 인경우")
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
}