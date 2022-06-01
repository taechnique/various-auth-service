package io.teach.business.member.service;

import io.teach.business.auth.dto.request.AuthRequestDto;
import io.teach.infrastructure.http.body.StandardResponse;
import org.springframework.stereotype.Service;

@Service(AppleAuthService.BEAN_NAME)
public class AppleAuthService extends AbstractAuthService {
    public static final String BEAN_NAME = "appleAuthService";

    public AppleAuthService() {
        System.out.println("AppleAuthService::new");
    }

    @Override
    public StandardResponse signup(AuthRequestDto dto) {
        return null;
    }

    @Override
    public StandardResponse authenticate(AuthRequestDto dto) {
        System.out.println("AppleAuthService -> authenticate()");
        return null;
    }
}
