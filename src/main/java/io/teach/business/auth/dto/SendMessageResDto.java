package io.teach.business.auth.dto;

import io.teach.business.auth.dto.response.CountModel;
import io.teach.infrastructure.http.body.StandardResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageResDto implements StandardResponse {

    @Valid
    @NotNull
    private CountModel count;

    @Min(1)
    private Integer expired;

    @NotBlank
    private String message;

    @NotBlank
    private String result;

    @Override
    public String result() {
        return this.result;
    }
}
