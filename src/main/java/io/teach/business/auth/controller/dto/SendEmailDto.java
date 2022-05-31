package io.teach.business.auth.controller.dto;

import io.teach.infrastructure.http.body.StandardRequest;
import io.teach.infrastructure.http.body.TrackingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailDto implements StandardRequest {

    private String email;

    private String group;

    private TrackingDto tracking;

    @Override
    public TrackingDto tracking() {
        return this.tracking;
    }
}
