package io.teach.infrastructure.excepted;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ServiceError {
    INVALID_REQUEST_BODY(400, 1, "올바르지 않은 요청 규격."),
    INVALID_REQUEST_HEADER(400, 2, "올바르지 않은 요청 헤더."),
    NOT_FOUND_APPROPRIATE_PROVIDER(400, 3, "존재하지 않는 서비스 제공자");

    private int status;
    private int resCode;
    private String cause;

    private static Map<Integer, ServiceError> errorMap = Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(ServiceError::getResCode, Function.identity())));

    ServiceError(final int status, final int resCode, final String cause) {
        this.status = status;
        this.resCode = resCode;
        this.cause = cause;
    }

    public static ServiceError findByCode(final int resCode) {
        return errorMap.get(resCode);
    }

}
