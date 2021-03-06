package io.teach.infrastructure.context.auth.impl;

import io.teach.business.auth.strategy.AuthStrategy;
import io.teach.infrastructure.context.auth.AuthStrategyContext;
import io.teach.infrastructure.context.auth.AuthStrategyContextHolderStrategy;
import io.teach.infrastructure.util.Util;
import org.springframework.util.Assert;

public class ThreadLocalAuthStrategyContextHolderStrategy implements AuthStrategyContextHolderStrategy {

    private static final ThreadLocal<AuthStrategyContext> contextHolder = new ThreadLocal<>();

    @Override
    public void clearContext() {
        contextHolder.remove();
    }

    @Override
    public AuthStrategyContext getContext() {
        AuthStrategyContext context = contextHolder.get();
        if(Util.isNull(context)) {
            context = createEmptyContext();
            setContext(context);
        }

        return context;
    }

    @Override
    public void setContext(AuthStrategyContext context) {
        Assert.notNull(context, "Only non-null AuthStrategyContext instances are permitted.");
        contextHolder.set(context);
    }

    @Override
    public AuthStrategyContext createEmptyContext() {
        return new AuthStrategyContextImpl(AuthStrategy.DEFAULT);
    }
}
