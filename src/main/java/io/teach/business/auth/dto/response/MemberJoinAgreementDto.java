package io.teach.business.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinAgreementDto {

    private Boolean concierge;
    private Boolean location;
    private Boolean marketingPrivacy;
    private Boolean marketingPush;
    private MemberAgreement member;
    private Boolean privacy;
    private Boolean push;
    private Boolean termOfService;

}
