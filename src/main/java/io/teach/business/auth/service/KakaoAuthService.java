package io.teach.business.auth.service;

import io.teach.business.auth.dto.AuthRequestDto;
import io.teach.infrastructure.http.body.StandardResponse;
import org.springframework.stereotype.Service;

@Service(KakaoAuthService.BEAN_NAME)
public class KakaoAuthService extends AbstractAuthService {

    public static final String BEAN_NAME = "kakaoAuthService";

    public KakaoAuthService() {
        System.out.println("KakaoAuthService::new");
    }


    @Override
    public StandardResponse signup(AuthRequestDto dto) {
        return null;
    }

    @Override
    public StandardResponse authenticate(AuthRequestDto dto) {

        System.out.println("KakaoAuthService -> authenticate()");

        return null;
    }
}
