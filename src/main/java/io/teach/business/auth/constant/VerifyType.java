package io.teach.business.auth.constant;

public enum VerifyType {
    EMAIL("이메일 인증 요청"),
    PHONE("휴대폰 인증 요청")
    ;

    private String description;

    VerifyType(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
