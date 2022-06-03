package io.teach.infrastructure.controller;

import lombok.Builder;

import java.util.Map;

public class CommonDocs {

    Map<String, String> errors;

    @Builder(builderClassName = "DocsBuilder", builderMethodName = "put", buildMethodName = "end")
    private CommonDocs(Map<String, String> errors) {
        this.errors = errors;
    }
}
