package io.teach.business.auth.constant;

public enum VerifyType {
    EMAIL("Send email for verify"),
    PHONE("Send SMS for verify")
    ;

    private String description;

    VerifyType(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
