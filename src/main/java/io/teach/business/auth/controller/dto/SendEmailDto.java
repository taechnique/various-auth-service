package io.teach.business.auth.controller.dto;

import io.teach.infrastructure.http.body.StandardRequest;
import io.teach.infrastructure.http.body.TrackingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailDto implements StandardRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String group;

    @Valid
    @NotNull
    private TrackingDto tracking;

    @Override
    public TrackingDto tracking() {
        return this.tracking;
    }
}
