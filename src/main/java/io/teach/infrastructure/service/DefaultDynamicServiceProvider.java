package io.teach.infrastructure.service;

import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.util.Util;

import javax.servlet.http.HttpServletRequest;

public class DefaultDynamicServiceProvider extends AbstractDynamicServiceProvider {

    public void judge(final HttpServletRequest req) throws AuthorizingException {
        if(Util.isNull(req))
            throw new AuthorizingException(ServiceStatus.INVALID_ACCESSED);

        final String requestURI = req.getRequestURI();

        if(requestURI.startsWith(AUTH_SERVICE_URI)) {
            super.checkAuthType(req);
        }

    }
}
