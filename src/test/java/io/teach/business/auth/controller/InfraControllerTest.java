package io.teach.business.auth.controller;

import io.teach.business.auth.controller.dto.SendEmailDto;
import io.teach.business.auth.dto.response.CountModel;
import io.teach.business.auth.dto.response.SendEmailResDto;
import io.teach.business.auth.dto.response.SendEmailResultDto;
import io.teach.infrastructure.controller.DefaultRestDocsConfiguration;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;

import static io.teach.infrastructure.excepted.ServiceStatus.success;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InfraControllerTest extends DefaultRestDocsConfiguration {


    @Test
    @DisplayName("[이메일 인증 요청] 인증 이메일 정상 전송")
    public void sendVerifyNumberForEmail() throws Throwable {
        /* Given */
        final SendEmailDto request = SendEmailDto.builder()
                .email("taechnique@yanolja.com")
                .group("JOIN")
                .tracking(defaultTracking())
                .build();

        final SendEmailResDto response = SendEmailResDto.builder()
                .result(success())
                .data(SendEmailResultDto.make(
                        6,
                        600,
                        "877ce73f-4e63-46b7-a26b-81e26ebb5la4",
                        CountModel.left(4)))
                .build();

        /* When */
        when(infraController.sendVerifyNumberForEmail(request))
                .thenReturn(ResponseEntity.ok(response));
        final ResultActions result = perform("/api/v1/infra/email/verify/send", HttpMethod.POST, request);

        /* Then */
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value(ServiceStatus.SUCCESS.getResult()))
                .andDo(this.handler.document(
                        requestFields(
                                field("email", "인증 요청 보낼 이메일"),
                                field("group", "인증 요청 그룹 (JOIN: 회원가입)"),
                                field("tracking.userAgent", "트래킹 정보 유저 에이전트"),
                                field("tracking.userIp", "클라이언트 아이피")
                        ),
                        responseFields(
                                field("result", "처리 결과"),
                                field("data.codeLength", "인증코드 길이"),
                                field("data.expired", "인증 유효시간 (초)"),
                                field("data.token", "발급된 인증 요청 토큰"),
                                field("data.count.remain", "남은 요청가능 횟수")
                        )
                ));

    }

    @Test
    @DisplayName("[이메일 인증요청] 하루 요청가능제한 초과")
    public void sendVerifyNumberForEmail1() throws Throwable {
        /* Given */
        final SendEmailDto request = SendEmailDto.builder()
                .email("taechnique@yanolja.com")
                .group("JOIN")
                .tracking(defaultTracking())
                .build();

        final ServiceStatus resErr = ServiceStatus.ALREADY_SPENT_ALL_EMAIL_CHANCE;

        /* When */
        when(infraController.sendVerifyNumberForEmail(request))
                .thenThrow(new AuthorizingException(resErr));
        final ResultActions result = errPerform("/api/v1/infra/email/verify/send", HttpMethod.POST, request);


        /* Then */
        result
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("result").value(resErr.getResult()))
                .andExpect(jsonPath("message").value(resErr.getMessage()))
                .andDo(this.handler.document(
                        requestFields(
                                field("email", "인증 요청 보낼 이메일"),
                                field("group", "인증 요청 그룹 (JOIN: 회원가입)"),
                                field("tracking.userAgent", "트래킹 정보 유저 에이전트"),
                                field("tracking.userIp", "클라이언트 아이피")
                        ),
                        errorFields()
                ));
    }


}