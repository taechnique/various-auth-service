package io.teach.business.auth.service;

import org.springframework.stereotype.Component;

@Component
public class AppleAuthService extends AbstractAuthService {

    public AppleAuthService() {
        System.out.println("AppleAuthService -> constructor");
    }

    @Override
    public void authenticate() {
        System.out.println("AppleAuthService -> authenticate()");

    }
}
