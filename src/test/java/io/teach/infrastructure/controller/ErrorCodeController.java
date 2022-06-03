package io.teach.infrastructure.controller;

import io.teach.infrastructure.excepted.ServiceStatus;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ErrorCodeController {


    @GetMapping(value = "/api/v1/docs/errors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonDocs> getErrorCodes() {


        return ResponseEntity.ok(CommonDocs.put()
                .errors(getDocuments(ServiceStatus.values()))
                .end());
    }

    private Map<String, String> getDocuments(ServiceStatus[] statuses) {
        return Arrays.stream(statuses)
                .collect(Collectors.toMap(ServiceStatus::name, ServiceStatus::getMessage));
    }
}
