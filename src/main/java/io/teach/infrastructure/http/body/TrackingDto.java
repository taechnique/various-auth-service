package io.teach.infrastructure.http.body;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class TrackingDto {

    @JsonInclude(Include.NON_NULL)
    private String userAgent;

    @JsonInclude(Include.NON_NULL)
    private String deviceOs;

    @JsonInclude(Include.NON_NULL)
    private String joinRoute;


    @NotEmpty
    private String userIp;

}
