package io.teach.business.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.teach.infrastructure.http.body.StandardResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class StandardAuthDto implements StandardResponse {

    @NotNull
    private String result;

    @NotNull
    private Long id;

    @NotEmpty
    @JsonProperty("access_token")
    private String accessToken;

    @Override
    public String result() {
        return this.result;
    }
}
