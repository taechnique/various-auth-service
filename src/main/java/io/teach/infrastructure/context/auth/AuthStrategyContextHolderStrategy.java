package io.teach.infrastructure.context.auth;

import io.teach.infrastructure.context.auth.AuthStrategyContext;

public interface AuthStrategyContextHolderStrategy {

    void clearContext();

    AuthStrategyContext getContext();

    void setContext(AuthStrategyContext context);

    AuthStrategyContext createEmptyContext();
}


