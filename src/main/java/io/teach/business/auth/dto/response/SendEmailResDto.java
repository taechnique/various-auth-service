package io.teach.business.auth.dto.response;

import io.teach.infrastructure.http.body.StandardResponse;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
public class SendEmailResDto implements StandardResponse {

    @NotEmpty
    private String result;

    private SendEmailResultDto data;

    @Override
    public String result() {
        return this.result;
    }
}
