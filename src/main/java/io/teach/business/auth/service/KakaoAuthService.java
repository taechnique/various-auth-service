package io.teach.business.auth.service;

import org.springframework.stereotype.Component;

@Component
public class KakaoAuthService extends AbstractAuthService {

    public KakaoAuthService() {
        System.out.println("KakaoAuthService -> constructor");
    }

    @Override
    public void authenticate() {
        System.out.println("KakaoAuthService -> authenticate()");
    }
}
