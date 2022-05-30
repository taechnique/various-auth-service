package io.teach.infrastructure.excepted;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ServiceErrorTest {

    @Test
    @DisplayName("에러코드 중복확인")
    public void checkDuplicatedErrorCode() {

        final Integer duplicatedKey = Stream.of(ServiceStatus.values())
                .collect(Collectors.groupingBy(ServiceStatus::getResCode, Collectors.counting()))
                .entrySet().stream().filter(e -> e.getValue() > 1).map(Map.Entry::getKey).findFirst().orElse(null);

        assertNull(duplicatedKey, String.format("ServiceError: \"%s\"이(가) 중복입니다.", ServiceStatus.findByCode(duplicatedKey)));
    }
}