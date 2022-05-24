package io.teach.infrastructure.holder;

public interface AuthStrategyContextHolderStrategy {

    void clearContext();

    AuthStrategyContext getContext();

    void setContext(AuthStrategyContext context);

    AuthStrategyContext createEmptyContext();
}


