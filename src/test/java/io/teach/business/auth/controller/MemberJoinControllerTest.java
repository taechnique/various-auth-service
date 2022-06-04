package io.teach.business.auth.controller;

import io.teach.business.auth.dto.request.ValidateDto;
import io.teach.business.auth.dto.response.ValidationResDto;
import io.teach.infrastructure.controller.DefaultRestDocsConfiguration;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.http.body.DefaultResponse;
import jdk.jshell.Snippet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.teach.infrastructure.excepted.ServiceStatus.success;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberJoinControllerTest extends DefaultRestDocsConfiguration {

    private static final String VALIDATE_ENDPOINT = "/api/v1/member/join/validate";

    @Test
    @DisplayName("[이메일 중복검사] 사용할 수 있는 이메일")
    public void checkDuplication() throws Throwable {
        /* Given */
        final ValidateDto request = ValidateDto.builder()
                .type("ID")
                .value("taechnique@yanolja.com")
                .tracking(defaultTracking())
                .build();

        final DefaultResponse response = DefaultResponse.ok();

        /* When */
        when(memberJoinController.checkDuplication(request))
                .thenReturn(ResponseEntity.ok(response));
        final ResultActions result = perform(VALIDATE_ENDPOINT, HttpMethod.POST, request);

        /* Then */
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value(success()))
                .andDo(this.handler.document(
                        requestFields(
                                field("type", "유효성 검증 타입"),
                                field("value", "검증 대상"),
                                field("tracking.userAgent", "트래킹 정보 유저 에이전트"),
                                field("tracking.userIp", "클라이언트 아이피")
                        ),
                        responseFields(
                                field("result", "처리 결과")
                        )
                ));
    }


    @Test
    @DisplayName("[이메일 중복검사] 잘못된 타입 또는 이메일")
    public void checkDuplication1() throws Throwable {
        /* Given */
        final ValidateDto request = ValidateDto.builder()
                .type("ACCOUNT")
                .value("taechnique@yanolja.com")
                .tracking(defaultTracking())
                .build();

        final ServiceStatus resErr = ServiceStatus.INVALID_PARAMETER;


        /* When */
        when(memberJoinController.checkDuplication(request))
                .thenThrow(new AuthorizingException(resErr));
        final ResultActions result = perform(VALIDATE_ENDPOINT, HttpMethod.POST, request);

        /* Then */
        result.andDo(print())
                .andExpect(status().is(resErr.getStatus()))
                .andExpect(jsonPath("result").value(resErr.getResult()))
                .andDo(this.handler.document(
                        requestFields(
                                field("type", "유효성 검증 타입"),
                                field("value", "검증 대상"),
                                field("tracking.userAgent", "트래킹 정보 유저 에이전트"),
                                field("tracking.userIp", "클라이언트 아이피")
                        ),
                        errorFields()
                ));
    }

    @Test
    @DisplayName("[이메일 중복확인] 이미 사용중인 아이디")
    public void checkDuplication2() throws Throwable {
        /* Given */
        final ValidateDto request = ValidateDto.builder()
                .type("ID")
                .value("taechnique@yanolja.com")
                .tracking(defaultTracking())
                .build();

        final ServiceStatus resErr = ServiceStatus.ALREADY_EXIST_LOGIN_ID;

        /* When */
        when(memberJoinController.checkDuplication(request))
                .thenThrow(new AuthorizingException(resErr));
        final ResultActions result = errPerform(VALIDATE_ENDPOINT, HttpMethod.POST, request);


        /* Then */
        result.andDo(print())
                .andExpect(status().is(resErr.getStatus()))
                .andExpect(jsonPath("result").value(resErr.getResult()))
                .andDo(this.handler.document(
                        requestFields(
                                field("type", "유효성 검증 타입"),
                                field("value", "검증 대상"),
                                field("tracking.userAgent", "트래킹 정보 유저 에이전트"),
                                field("tracking.userIp", "클라이언트 아이피")
                        ),
                        errorFields()
                ));
    }
}