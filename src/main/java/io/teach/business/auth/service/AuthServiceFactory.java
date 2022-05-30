package io.teach.business.auth.service;

import io.teach.business.auth.strategy.AuthStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;

@Component
@RequiredArgsConstructor
public class AuthServiceFactory {

    private final ApplicationContext context;

    public AuthService retrieve(final AuthStrategy strategy) {

        final String beanName = AuthStrategy.find(strategy);
        final AuthService toBeInjected = (AuthService) context.getBean(beanName);

        return toBeInjected;
    }
}
