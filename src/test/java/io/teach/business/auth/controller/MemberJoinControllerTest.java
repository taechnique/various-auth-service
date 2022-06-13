package io.teach.business.auth.controller;

import io.teach.business.auth.dto.AgreementModel;
import io.teach.business.auth.dto.MemberJoinDto;
import io.teach.business.auth.dto.request.ValidateDto;
import io.teach.business.auth.dto.response.*;
import io.teach.infrastructure.controller.DefaultRestDocsConfiguration;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.http.body.DefaultResponse;
import io.teach.infrastructure.http.body.TrackingDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.snippet.Snippet;
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
    public void validateEmail() throws Throwable {
        /* Given */
        final ValidateDto request = ValidateDto.builder()
                .type("ID")
                .value("taechnique@yanolja.com")
                .tracking(defaultTracking())
                .build();

        final DefaultResponse response = DefaultResponse.ok();

        /* When */
        when(memberJoinController.validate(request))
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
    public void validateEmail1() throws Throwable {
        /* Given */
        final ValidateDto request = ValidateDto.builder()
                .type("ACCOUNT")
                .value("taechnique@yanolja.com")
                .tracking(defaultTracking())
                .build();

        final ServiceStatus resErr = ServiceStatus.INVALID_PARAMETER;


        /* When */
        when(memberJoinController.validate(request))
                .thenThrow(new AuthorizingException(resErr));
        final ResultActions result = errPerform(VALIDATE_ENDPOINT, HttpMethod.POST, request);

        /* Then */
        result.andDo(print())
                .andExpect(status().is(resErr.getStatus()))
                .andExpect(jsonPath("result").value(resErr.getResult()))
                .andDo(this.errorHandler.document(
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
    public void validateEmail2() throws Throwable {
        /* Given */
        final ValidateDto request = ValidateDto.builder()
                .type("ID")
                .value("taechnique@yanolja.com")
                .tracking(defaultTracking())
                .build();

        final ServiceStatus resErr = ServiceStatus.ALREADY_EXIST_LOGIN_ID;

        /* When */
        when(memberJoinController.validate(request))
                .thenThrow(new AuthorizingException(resErr));
        final ResultActions result = errPerform(VALIDATE_ENDPOINT, HttpMethod.POST, request);


        /* Then */
        result.andDo(print())
                .andExpect(status().is(resErr.getStatus()))
                .andExpect(jsonPath("result").value(resErr.getResult()))
                .andDo(this.errorHandler.document(
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
    @DisplayName("[비밀번호 유효성 검사] 사용 가능한 비밀번호")
    public void validatePassword() throws Throwable {
        /* Given */
        final ValidateDto request = ValidateDto.builder()
                .type("PASSWORD")
                .value("yanolj4ever!")
                .tracking(defaultTracking())
                .build();

        final DefaultResponse response = DefaultResponse.ok();

        /* When */
        when(memberJoinController.validate(request))
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
    @DisplayName("[비밀번호 유효성 검사] 잘못된 비밀번호 형식")
    public void validatePassword1() throws Throwable {
        /* Given */
        final ValidateDto request = ValidateDto.builder()
                .type("PASSWORD")
                .value("yanolja4ver")
                .tracking(defaultTracking())
                .build();

        final ServiceStatus resErr = ServiceStatus.INVALID_PASSWORD_FORMAT;

        /* When */
        when(memberJoinController.validate(request))
                .thenThrow(new AuthorizingException(resErr));
        final ResultActions result = errPerform(VALIDATE_ENDPOINT, HttpMethod.POST, request);

        /* Then */
        result.andDo(print())
                .andExpect(status().is(resErr.getStatus()))
                .andExpect(jsonPath("result").value(resErr.getResult()))
                .andDo(this.errorHandler.document(
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
    @DisplayName("[일반 회원가입] 정상가입")
    public void memberJoinOfVersion4() throws Throwable {
        /* Given */
        final MemberJoinDto request = joinRequest();

        final RegisteredResDto response = RegisteredResDto.builder()
                .data(RegisteredResultDto.builder()
                        .agreements(MemberJoinAgreementDto.builder()
                                .location(true)
                                .marketingPrivacy(true)
                                .marketingPush(true)
                                .member(MemberAgreement.builder()
                                        .email(true)
                                        .inactivity(true)
                                        .sms(true)
                                        .build())
                                .privacy(true)
                                .termOfService(true)
                                .build())
                        .appServiceAgreed(true)
                        .hashedMemberNo("ee58984b67444628b72356d3510c3bee")
                        .id("taechnique@yanolja.com")
                        .joinedAtISO8601("2022-06-13T19:40:39.382382")
                        .memberNo(1L)
                        .memberType("YANOLJA")
                        .nickName("합류준비완료01")
                        .phoneNum("01082827070")
                        .phoneNumYN("Y")
                        .token("eyJ5bm1ubyI6MSwieWlkIjoiYTk0OGUzNjllMzI4NDgxNWQ4NzFjZDhkMDY0ZTY5MDYiLCJtbm8iOjF9.1ef0d8c334010760b29bc750a5ffc96b819049ae3839db919fefb0972aaad5e6")
                        .build())
                .result(success())
                .build();

        /* When */
        when(memberJoinController.memberJoinOfVersion4(request))
                .thenReturn(ResponseEntity.ok(response));
        final ResultActions result = perform("/api/v1/member/join/v4", HttpMethod.POST, request);

        /* Then */
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value(success()))
                . andDo(this.handler.document(
                        joinRequestSnippets()
                        ,
                        responseFields(
                                field("data.agreements.concierge", "컨시어지 이용 동의?", true),
                                field("data.agreements.location", "위치정보 이용 동의여부", true),
                                field("data.agreements.marketingPrivacy", "개인정보 마케팅 이용 동의여부", true),
                                field("data.agreements.marketingPush", "마케팅 알림 수신여부", true),
                                field("data.agreements.member.email", "이메일 수신 동의 여부", true),
                                field("data.agreements.member.inactivity", "장기 미접속시 계정 유지 동의 여부", true),
                                field("data.agreements.member.prepayment", "선불결제 동의 여부", true),
                                field("data.agreements.member.sms", "SMS 수신 동의 여부", true),
                                field("data.agreements.privacy", "개인정보 수집 및 이용동의 여부", true),
                                field("data.agreements.push", "앱 푸시 알림 여부", true),
                                field("data.agreements.termOfService", "서비스 이용약관 동의 여부", true),
                                field("data.appServiceAgreed", "앱 서비스 이용동의", true),
                                field("data.hashedMemberNo", "멤버 아이디 해쉬값"),
                                field("data.id", "로그인 서비스 아이디"),
                                field("data.joinedAtISO8601", "가입 일시 (ISO8601)"),
                                field("data.memberNo", "멤버 고유키"),
                                field("data.memberType", "멤버 타입"),
                                field("data.nickName", "멤버 닉네임"),
                                field("data.phoneNum", "휴대폰 번호"),
                                field("data.phoneNumYN", "휴대폰 인증 여부"),
                                field("data.token", "발급된 YWT"),
                                field("result", "처리 결과")
                        )

                ));

    }
    @Test
    @DisplayName("[회원가입] 필수항목의 동의가 되어있지 않은 경우")
    public void memberJoinOfVersion4_1() throws Throwable {
        /* Given */
        final MemberJoinDto request = joinRequest();
        final ServiceStatus resErr = ServiceStatus.NEED_ESSENTIAL_AGREEMENT;

        /* When */
        when(memberJoinController.memberJoinOfVersion4(request))
                .thenThrow(new AuthorizingException(resErr));
        final ResultActions result = errPerform("/api/v1/member/join/v4", HttpMethod.POST, request);

        /* Then */
        result
                .andDo(print())
                .andExpect(status().is(resErr.getStatus()))
                .andExpect(jsonPath("result").value(resErr.getResult()))
                .andDo(this.errorHandler.document(
                        joinRequestSnippets(),
                        errorFields()
                ));

    }

    @Test
    @DisplayName("[회원가입] 입력값이 올바르지 않은경우")
    public void memberJoinOfVersion4_2() throws Throwable {
        /* Given */
        final MemberJoinDto request = joinRequest();
        final ServiceStatus resErr = ServiceStatus.INVALID_PARAMETER;

        /* When */
        when(memberJoinController.memberJoinOfVersion4(request))
                .thenThrow(new AuthorizingException(resErr));
        final ResultActions result = errPerform("/api/v1/member/join/v4", HttpMethod.POST, request);

        /* Then */
        result
                .andDo(print())
                .andExpect(status().is(resErr.getStatus()))
                .andExpect(jsonPath("result").value(resErr.getResult()))
                .andDo(this.errorHandler.document(
                        joinRequestSnippets(),
                        errorFields()
                ));
    }

    public static MemberJoinDto joinRequest() {
        final TrackingDto tracking = new TrackingDto();
        tracking.setDeviceOs("PW");
        tracking.setJoinRoute("PC2");
        tracking.setUserIp("127.0.0.1");

        final MemberJoinDto request = MemberJoinDto.builder()
                .agreements(AgreementModel.builder()
                        .inactivity(true)
                        .location(true)
                        .marketingPrivacy(true)
                        .privacy(true)
                        .promotion(true)
                        .termOfService(true)
                        .build())
                .certifyCode("123456")
                .email("taechnique@yanolja.com")
                .emailToken("c6ece304-22da-4fbd-bc3a-96316c562cad")
                .password("yanolja4ever!")
                .passwordConfirm("yanolja4ever!")
                .phone("010-8282-7070")
                .tracking(tracking)
                .build();
        return request;
    }

    public Snippet joinRequestSnippets() {
        return requestFields(
                field("agreements.inactivity", "장기 미접속 시에도 계정 유지(선택)", true),
                field("agreements.location", "위치 정보 이용 약관 동의(선택)", true),
                field("agreements.marketingPrivacy", "(마케팅?) 개인정보 이용동의(선택)", true),
                field("agreements.privacy", "개인정보 수집 동의(선택)", true),
                field("agreements.promotion", "프로모션 정보 수신동의(선택)", true),
                field("agreements.termOfService", "서비스 이용약관 이용동(선택)", true),
                field("certifyCode", "휴대폰 인증번호"),
                field("email", "인증된 이메일"),
                field("emailToken", "인증된 이메일 토큰"),
                field("password", "비밀번호"),
                field("passwordConfirm", "비밀번호 확인"),
                field("phone", "인증에 사용한 휴대폰 번호"),
                field("tracking.deviceOs", "디바이스 OS"),
                field("tracking.joinRoute", "가입 경로"),
                field("tracking.userIp", "유저 아이피")
        );
    }
}