package io.teach.infrastructure.holder;

import io.teach.business.auth.strategy.AuthStrategy;

public interface AuthStrategyContext {

    AuthStrategy getStrategy();

    void setStrategy(AuthStrategy strategy);
}
