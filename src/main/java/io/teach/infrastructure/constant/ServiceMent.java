package io.teach.infrastructure.constant;

public enum ServiceMent {
    SEND_VERIFY_NUMBER("인증번호가 발송되었습니다 (남은 횟수 %d회)"),
    PASSED_PHONE_CERTIFY_FAILED("인증번호가 올바르지 않습니다. (남은 재발송 횟수 0회"),
    ;

    private String ment;

    ServiceMent(final String ment) {
        this.ment = ment;
    }

    public String form() {
        return this.ment;
    }
}
