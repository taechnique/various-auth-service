package io.teach.infrastructure.interceptor;

import io.teach.business.auth.strategy.AuthStrategy;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.service.StrategyService;
import io.teach.infrastructure.util.Util;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String obtainedAuthType = request.getHeader("X-AUTH-TYPE");
        if(Util.isNull(obtainedAuthType))
            throw new AuthorizingException(ServiceStatus.INVALID_REQUEST_HEADER);

        final String xAuthType = obtainedAuthType.toUpperCase();
        AuthStrategy authStrategy = AuthStrategy.find(xAuthType);
        if(Util.isNull(authStrategy))
            throw new AuthorizingException(ServiceStatus.NOT_FOUND_APPROPRIATE_PROVIDER);
        System.out.println("AuthCheckInterceptor <- authStrategy: " + authStrategy);
        StrategyService.apply(authStrategy);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
