package io.teach.business.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAgreement {

    private Boolean email;
    private Boolean inactivity;
    private Boolean prepayment;
    private Boolean sms;

}
