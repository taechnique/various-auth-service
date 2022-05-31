package io.teach.business.auth.dto;

import io.teach.infrastructure.http.body.StandardRequest;
import io.teach.infrastructure.http.body.TrackingDto;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ValidateDto implements StandardRequest {

    @NotEmpty
    private String type;

    @NotEmpty
    private String value;

    @Valid
    @NotNull
    private TrackingDto tracking;

    @Override
    public TrackingDto tracking() {
        return this.tracking;
    }
}
