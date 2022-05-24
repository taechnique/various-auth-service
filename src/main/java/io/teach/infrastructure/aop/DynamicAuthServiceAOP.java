package io.teach.infrastructure.aop;

import io.teach.business.auth.config.AuthBusinessConfiguration;
import io.teach.business.auth.controller.AuthorizationController;
import io.teach.business.auth.dto.AuthRequestDto;
import io.teach.business.auth.service.AuthServiceFactory;
import io.teach.business.auth.strategy.AuthStrategy;
import io.teach.infrastructure.context.auth.AuthStrategyContextHolder;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DynamicAuthServiceAOP {

    private final AuthServiceFactory factory;

    @Before("execution(* io.teach.business.auth.controller.AuthorizationController.*(..)) and args(authDto)")
    public void prepareAuthService(JoinPoint joinPoint, final AuthRequestDto authDto) throws Throwable {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AuthBusinessConfiguration.class);
        final AuthorizationController controller = context.getBean(AuthorizationController.class);
        System.out.println("controller = " + controller);
        final AuthStrategy strategy = AuthStrategyContextHolder.getContext().getStrategy();
        controller.setDynamicAuthService(factory.create(strategy));


    }
}
