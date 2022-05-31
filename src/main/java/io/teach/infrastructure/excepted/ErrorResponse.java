package io.teach.infrastructure.excepted;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.teach.infrastructure.http.body.StandardResponse;

public class ErrorResponse implements StandardResponse {

    @JsonProperty("result")
    private String result;

    @JsonProperty("message")
    @JsonInclude(Include.NON_EMPTY)
    private String message;

    private ErrorResponse(final String result, final String message) {
        this.result = result;
        this.message = message;
    }

    public static ErrorResponse create(final ServiceStatus error) {
        return new ErrorResponse(error.getResult(), error.getMessage());
    }

    @Override
    public String result() {
        return this.result;
    }

}
