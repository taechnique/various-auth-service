package io.teach.business.auth.strategy;

import io.teach.business.auth.service.AppleAuthService;
import io.teach.business.auth.service.AuthService;
import io.teach.business.auth.service.DefaultAuthService;
import io.teach.business.auth.service.KakaoAuthService;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AuthStrategy {
    DEFAULT ("DEFAULT", DefaultAuthService.class),
    KAKAO ("KAKAO", KakaoAuthService.class),
    APPLE ("APPLE", AppleAuthService.class);

    String serviceName;
    Class<? extends AuthService> authService;
    private static final Map<String, AuthStrategy> serviceTickets = Arrays.stream(values())
            .collect(Collectors.toMap(AuthStrategy::getServiceName, Function.identity()));

    AuthStrategy(String serviceName, Class<? extends AuthService> authService) {
        this.serviceName = serviceName;
        this.authService = authService;
    }

    public String getServiceName() {
        return this.serviceName;
    }
    public Class<? extends AuthService> getAuthService() {
        return this.authService;
    }

    public static Class<? extends AuthService> find(final AuthStrategy authStrategy) {
        return find(authStrategy.getServiceName()).getAuthService();
    }

    public static AuthStrategy find(final String serviceName) {
        return serviceTickets.get(serviceName);
    }
}
