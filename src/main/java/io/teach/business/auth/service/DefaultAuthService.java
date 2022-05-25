package io.teach.business.auth.service;

import io.teach.business.auth.dto.AuthRequestDto;
import io.teach.business.auth.repository.AuthRepository;
import io.teach.infrastructure.http.body.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service(DefaultAuthService.BEAN_NAME)
@RequiredArgsConstructor
public class DefaultAuthService extends AbstractAuthService {

    public static final String BEAN_NAME = "defaultAuthService";
    private final AuthRepository repo;

    @Override
    public StandardResponse authenticate(final AuthRequestDto dto) {
        System.out.println("DefaultAuthService -> authenticate()");



        return null;
    }
}
