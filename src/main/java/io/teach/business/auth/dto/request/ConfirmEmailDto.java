package io.teach.business.auth.dto.request;

import io.teach.infrastructure.http.body.StandardRequest;
import io.teach.infrastructure.http.body.TrackingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
public class ConfirmEmailDto implements StandardRequest {

    @NotEmpty
    @Pattern(regexp = "([0-9]{6})")
    private String code;

    @NotEmpty
    private String token;

    @NotNull
    @Valid
    private TrackingDto tracking;

    @Override
    public TrackingDto tracking() {
        return this.tracking;
    }
}
