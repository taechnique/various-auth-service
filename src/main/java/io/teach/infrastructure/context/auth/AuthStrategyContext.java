package io.teach.infrastructure.context.auth;

import io.teach.business.auth.strategy.AuthStrategy;

public interface AuthStrategyContext {

    AuthStrategy getStrategy();

    void setStrategy(AuthStrategy strategy);
}
