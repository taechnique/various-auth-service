package io.teach.business.auth.dto.response;

import io.teach.infrastructure.http.body.StandardResponse;
import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class SendEmailResDto implements StandardResponse {

    @NotEmpty
    private String result;

    @NotNull
    @Valid
    private SendEmailResultDto data;

    @Override
    public String result() {
        return this.result;
    }
}
