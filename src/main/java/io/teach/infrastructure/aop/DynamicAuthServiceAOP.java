package io.teach.infrastructure.aop;

import io.teach.business.member.controller.AuthorizationController;
import io.teach.business.auth.dto.request.AuthRequestDto;
import io.teach.business.member.service.AuthService;
import io.teach.business.member.service.AuthServiceFactory;
import io.teach.business.auth.strategy.AuthStrategy;
import io.teach.infrastructure.context.auth.AuthStrategyContextHolder;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@RequiredArgsConstructor
public class DynamicAuthServiceAOP {

    private final AuthServiceFactory factory;
    private final ApplicationContext context;

    @Before("execution(* io.teach.business.member.controller.AuthorizationController.*(..)) and args(authDto)")
    public void prepareAuthService(final JoinPoint joinPoint, final AuthRequestDto authDto) throws Throwable {


        final AuthStrategy strategy = AuthStrategyContextHolder.getContext().getStrategy();
        final AuthService authService = factory.retrieve(strategy);
        final AuthorizationController controller = (AuthorizationController) context.getBean(AuthorizationController.BEAN_NAME);
        controller.setDynamicAuthService(authService);

    }
}
