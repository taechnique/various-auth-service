package io.teach.business.auth.dto;

import io.teach.infrastructure.http.body.StandardRequest;
import io.teach.infrastructure.http.body.TrackingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
public class MemberJoinDto implements StandardRequest {

    @NotNull
    @Valid
    private AgreementModel agreements;

    @NotBlank
    @Pattern(regexp = "([0-9]{6})")
    private String certifyCode;

    @NotBlank
    private String email;

    @NotBlank
    private String emailToken;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    @NotBlank
    private String phone;

    @NotNull
    @Valid
    private TrackingDto tracking;

    @Override
    public TrackingDto tracking() {
        return this.tracking;
    }
}
