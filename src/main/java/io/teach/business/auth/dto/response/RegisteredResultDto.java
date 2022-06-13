package io.teach.business.auth.dto.response;

import io.teach.business.auth.dto.AgreementModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredResultDto {

    private MemberJoinAgreementDto agreements;
    private Boolean appServiceAgreed;
    private String hashedMemberNo;
    private String id;
    private String joinedAtISO8601;
    private Long memberNo;
    private String memberType;
    private String nickName;
    private String phoneNum;
    private String phoneNumYN;
    private String token;
}
