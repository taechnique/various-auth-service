package io.teach.infrastructure.context.auth;

import io.teach.business.auth.strategy.AuthStrategy;
import io.teach.infrastructure.context.auth.AuthStrategyContext;
import io.teach.infrastructure.context.auth.AuthStrategyContextHolderStrategy;
import io.teach.infrastructure.context.auth.impl.ThreadLocalAuthStrategyContextHolderStrategy;
import org.springframework.util.StringUtils;

public class AuthStrategyContextHolder {

    private static AuthStrategyContextHolderStrategy strategy;


    static {
        init();
    }

    public static void init() {
        strategy = new ThreadLocalAuthStrategyContextHolderStrategy();
    }

    public static void clearContext() {
        strategy.clearContext();
    }

    public static AuthStrategyContext getContext() {
        return strategy.getContext();
    }

    public static void setContext(AuthStrategyContext context) {
        strategy.setContext(context);
    }

    public static AuthStrategyContext createEmptyContext() {
        AuthStrategyContext emptyContext = strategy.createEmptyContext();
        return emptyContext;
    }
}
