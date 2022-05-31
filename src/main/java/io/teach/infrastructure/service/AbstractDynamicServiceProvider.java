package io.teach.infrastructure.service;

import io.teach.business.auth.strategy.AuthStrategy;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.util.Util;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractDynamicServiceProvider implements DynamicServiceProvider {

    protected static final String AUTH_SERVICE_URI = "/api/v1/user";

    public void checkAuthType(final HttpServletRequest request) throws AuthorizingException {
        final String obtainedAuthType = request.getHeader("X-AUTH-TYPE");

        if(Util.isNull(obtainedAuthType))
            throw new AuthorizingException(ServiceStatus.INVALID_REQUEST_HEADER);

        final String xAuthType = obtainedAuthType.toUpperCase();
        AuthStrategy authStrategy = AuthStrategy.find(xAuthType);

        if(Util.isNull(authStrategy))
            throw new AuthorizingException(ServiceStatus.NOT_FOUND_APPROPRIATE_PROVIDER);

        System.out.println("AuthCheckInterceptor <- authStrategy: " + authStrategy);
        StrategyService.apply(authStrategy);
    }

}
