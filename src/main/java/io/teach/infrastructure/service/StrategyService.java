package io.teach.infrastructure.service;

import io.teach.business.auth.strategy.AuthStrategy;
import io.teach.infrastructure.context.auth.AuthStrategyContext;
import io.teach.infrastructure.context.auth.AuthStrategyContextHolder;
import io.teach.infrastructure.util.Util;

public class StrategyService {

    public static void apply(final AuthStrategy strategy) throws Exception {

        AuthStrategyContextHolder.clearContext();

        AuthStrategyContext context = AuthStrategyContextHolder.createEmptyContext();
        if (Util.isNotNull(strategy))
            context.setStrategy(strategy);
        AuthStrategyContextHolder.setContext(context);
    }
}
