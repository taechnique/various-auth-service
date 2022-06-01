package io.teach.business.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.teach.infrastructure.http.body.StandardResponse;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AuthResponseDto implements StandardResponse {

    @NotNull
    private String result;

    @JsonInclude(Include.NON_NULL)
    private String usable;

    @Override
    public String result() {
        return this.result;
    }
}
