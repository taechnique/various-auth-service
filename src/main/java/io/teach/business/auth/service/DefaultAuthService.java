package io.teach.business.auth.service;

import org.springframework.stereotype.Component;

@Component
public class DefaultAuthService extends AbstractAuthService {

    public DefaultAuthService() {
        System.out.println("DefaultAuthService -> constructor");
    }

    @Override
    public void authenticate() {
        System.out.println("DefaultAuthService -> authenticate()");
    }
}
