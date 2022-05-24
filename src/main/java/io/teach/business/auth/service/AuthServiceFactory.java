package io.teach.business.auth.service;

import io.teach.business.auth.strategy.AuthStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;

@Component
public class AuthServiceFactory {

    public AuthService create(final AuthStrategy strategy) {
        AuthService toBeInjectedService = null;
        try {

            final Class<? extends AuthService> authService = AuthStrategy.find(strategy);
            final Constructor<? extends AuthService> constructor = authService.getConstructor();

            toBeInjectedService = constructor.newInstance();
            System.out.println("toBeInjectedService = " + toBeInjectedService);
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return toBeInjectedService;
    }
}
