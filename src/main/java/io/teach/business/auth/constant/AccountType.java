package io.teach.business.auth.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AccountType {
    ID("ID"),
    PASSWORD("PASSWORD")
    ;

    private String type;
    private static final Map<String, AccountType> typeMap = Arrays.stream(values()).collect(Collectors.toMap(AccountType::getType, Function.identity()));

    AccountType(final String type){
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static AccountType typeOf(final String type) {
        return typeMap.get(type);
    }
}
