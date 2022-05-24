package io.teach.infrastructure.context.auth.impl;

import io.teach.business.auth.strategy.AuthStrategy;
import io.teach.infrastructure.context.auth.AuthStrategyContext;
import io.teach.infrastructure.util.Util;
import org.springframework.util.ObjectUtils;

public class AuthStrategyContextImpl implements AuthStrategyContext {

    private AuthStrategy strategy;

    public AuthStrategyContextImpl() {
        System.out.println("AuthStrategyContextImpl - constructor");
    }

    public AuthStrategyContextImpl(AuthStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AuthStrategyContextImpl) {
            AuthStrategyContextImpl another = (AuthStrategyContextImpl) obj;
            if(Util.isNull(this.getStrategy()) && Util.isNull(another.getStrategy())) {
                return true;
            }
            if(Util.isNotNull(this.getStrategy()) && Util.isNotNull(another.getStrategy()) && this.getStrategy().equals(another.getStrategy())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.strategy);
    }

    @Override
    public AuthStrategy getStrategy() {
        return this.strategy;
    }

    @Override
    public void setStrategy(AuthStrategy strategy) {
        this.strategy = strategy;
    }
}
