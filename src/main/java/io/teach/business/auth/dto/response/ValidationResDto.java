package io.teach.business.auth.dto.response;

import io.teach.infrastructure.http.body.StandardResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationResDto implements StandardResponse {

    private String result;

    @Override
    public String result() {
        return this.result;
    }
}
