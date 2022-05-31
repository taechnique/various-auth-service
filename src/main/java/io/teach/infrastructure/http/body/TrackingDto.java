package io.teach.infrastructure.http.body;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingDto {

    @NotEmpty
    private String userAgent;

    @NotEmpty
    private String userIp;
}
