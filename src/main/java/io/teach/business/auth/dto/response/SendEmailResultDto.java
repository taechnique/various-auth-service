package io.teach.business.auth.dto.response;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendEmailResultDto {
    @NotNull
    private Integer codeLength;

    @NotNull
    private Integer expired;

    @NotNull
    private String token;

    @NotNull
    private CountModel count;

    public static SendEmailResultDto make(final Integer codeLength, final Integer expired, final String token, final CountModel count) {
        final SendEmailResultDto dto = new SendEmailResultDto();
        dto.codeLength = codeLength;
        dto.expired = expired;
        dto.token = token;
        dto.count = count;

        return dto;
    }
}
