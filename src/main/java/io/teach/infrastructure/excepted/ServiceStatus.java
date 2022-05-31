package io.teach.infrastructure.excepted;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ServiceStatus {
    SUCCESS(200, 0,"SUCCESS", "성공 응답"),
    INVALID_REQUEST_BODY(400, 1,"INVALID_REQUEST_BODY", "올바르지 않은 요청 규격"),
    INVALID_REQUEST_HEADER(400, 2,"INVALID_REQUEST_HEADER", "올바르지 않은 요청 헤더"),
    NOT_FOUND_APPROPRIATE_PROVIDER(400, 3,"NOT_FOUND_APPROPRIATE_PROVIDER", "존재하지 않는 서비스 제공자"),
    NOT_FOUND_REQUESTED_TYPE(400, 4,"NOT_FOUND_REQUESTED_TYPE", "잘못된 요청 타입"),
    INVALID_EMAIL_FORM(400, 5,"INVALID_EMAIL_FORM", "올바르지 않은 이메일 형식"),
    ALREADY_EXIST_LOGIN_ID(409, 6,"FAILED", "사용할 수 없는 회원 정보입니다. 다른정보로 다시 시도해주세요."),
    INVALID_PARAMETER(400, 7,"FAILED", "잘못된 입력값"),
    INVALID_ACCESSED(405, 8, "FAILED", "잘못된 접근");


    private int status;
    private int resCode;
    private String result;
    private String message;

    private static Map<Integer, ServiceStatus> statusMap = Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(ServiceStatus::getResCode, Function.identity())));

    ServiceStatus(final int status, final int resCode, final String result, final String message) {
        this.status = status;
        this.resCode = resCode;
        this.result = result;
        this.message = message;
    }

    public static ServiceStatus findByCode(final int resCode) {
        return statusMap.get(resCode);
    }

    public static String success() {
        return statusMap.get(0).result;
    }

}
