package io.teach.infrastructure.excepted;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.teach.infrastructure.http.body.StandardResponse;

public class ErrorResponse implements StandardResponse {

    @JsonProperty("err_code")
    private Integer resCode;

    @JsonProperty("cause")
    @JsonInclude(Include.NON_EMPTY)
    private String cause;

    private ErrorResponse(final int resCode, final String cause) {
        this.resCode = resCode;
        this.cause = cause;
    }

    public static ErrorResponse create(final ServiceError error) {
        return new ErrorResponse(error.getResCode(), error.getCause());
    }

    @Override
    public Integer resCode() {
        return this.resCode;
    }

    @Override
    public String toString() {
        return String.format("{\n\t\"res_code\": %d,\n\t\"cause\": \"%s\"\n}\n", this.resCode, this.cause);
    }
}
