package io.teach.infrastructure.excepted;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ServiceStatus {
    SUCCESS(200, 0, "성공 응답"),
    INVALID_REQUEST_BODY(400, 1, "올바르지 않은 요청 규격"),
    INVALID_REQUEST_HEADER(400, 2, "올바르지 않은 요청 헤더"),
    NOT_FOUND_APPROPRIATE_PROVIDER(400, 3, "존재하지 않는 서비스 제공자"),
    NOT_FOUND_REQUESTED_TYPE(400, 4, "잘못된 요청 타입"),
    INVALID_EMAIL_FORM(400, 5, "올바르지 않은 이메일 형식"),
    ALREADY_EXIST_EMAIL(409, 6, "사용중인 이메일입니다.");

    private int status;
    private int resCode;
    private String cause;

    private static Map<Integer, ServiceStatus> errorMap = Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(ServiceStatus::getResCode, Function.identity())));

    ServiceStatus(final int status, final int resCode, final String cause) {
        this.status = status;
        this.resCode = resCode;
        this.cause = cause;
    }

    public static ServiceStatus findByCode(final int resCode) {
        return errorMap.get(resCode);
    }

}
