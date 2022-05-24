package io.teach.infrastructure.interceptor;

import io.teach.business.auth.strategy.AuthStrategy;
import io.teach.infrastructure.service.StrategyService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String xAuthType = request.getHeader("X-AUTH-TYPE").toUpperCase();
        AuthStrategy authStrategy = AuthStrategy.find(xAuthType);
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
