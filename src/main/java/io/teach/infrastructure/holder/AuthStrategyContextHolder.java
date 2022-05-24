package io.teach.infrastructure.holder;

public class AuthStrategyContextHolder {

    private static AuthStrategyContextHolderStrategy strategy;


    static {

    }

    public static void init() {
    }

    public static void clearContext() {

    }

    public static AuthStrategyContext getContext() {
        return null;
    }

    public static void setContext(AuthStrategyContext context) {

    }

    public static AuthStrategyContext createEmptyContext() {
        return null;
    }
}
