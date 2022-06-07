package io.teach.business.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class AgreementModel {

    @NotNull
    private Boolean inactivity;

    @NotNull
    private Boolean location;

    @NotNull
    private Boolean marketingPrivacy;

    @NotNull
    private Boolean privacy;

    @NotNull
    private Boolean promotion;

    @NotNull
    private Boolean termOfService;
}
