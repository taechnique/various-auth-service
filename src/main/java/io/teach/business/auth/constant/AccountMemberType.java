package io.teach.business.auth.constant;

public enum AccountMemberType {
    YANOLJA("YANOLJA"),
    INTERPARK("INTERPARK"),
    ;

    private String typeName;


    AccountMemberType(final String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return this.typeName;
    }
}
