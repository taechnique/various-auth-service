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
    private static final Map<String, Class<? extends AuthService>> serviceTickets = Arrays.stream(values())
            .collect(Collectors.toMap(AuthStrategy::getServiceName, AuthStrategy::getAuthService));

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
        return serviceTickets.get(authStrategy.getServiceName());
    }
}
