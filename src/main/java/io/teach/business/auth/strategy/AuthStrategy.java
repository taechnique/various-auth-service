package io.teach.business.auth.strategy;

import io.teach.business.member.service.AppleAuthService;
import io.teach.business.member.service.DefaultAuthService;
import io.teach.business.member.service.KakaoAuthService;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AuthStrategy {
    DEFAULT ("DEFAULT", DefaultAuthService.BEAN_NAME),
    KAKAO ("KAKAO", KakaoAuthService.BEAN_NAME),
    APPLE ("APPLE", AppleAuthService.BEAN_NAME);

    String serviceName;
    String beanName;

    private static final Map<String, AuthStrategy> serviceTickets = Arrays.stream(values())
            .collect(Collectors.toMap(AuthStrategy::getServiceName, Function.identity()));

    AuthStrategy(String serviceName, String beanName) {
        this.serviceName = serviceName;
        this.beanName = beanName;
    }

    public String getServiceName() {
        return this.serviceName;
    }
    public String getBeanName() {
        return beanName;
    }

    public static String find(final AuthStrategy authStrategy) {
        return find(authStrategy.getServiceName()).getBeanName();
    }

    public static AuthStrategy find(final String serviceName) {
        return serviceTickets.get(serviceName);
    }
}
