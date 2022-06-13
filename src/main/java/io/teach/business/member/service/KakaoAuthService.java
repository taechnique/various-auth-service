package io.teach.business.member.service;

import io.teach.business.auth.dto.request.AuthRequestDto;
import io.teach.business.auth.entity.VerifyInfo;
import io.teach.business.auth.repository.AuthHistoryRepository;
import io.teach.business.auth.repository.VerifyInfoRepository;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.http.body.DefaultResponse;
import io.teach.infrastructure.http.body.StandardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service(KakaoAuthService.BEAN_NAME)
@RequiredArgsConstructor
public class KakaoAuthService extends AbstractAuthService {

    public static final String BEAN_NAME = "kakaoAuthService";
    private final VerifyInfoRepository verifyInfoRepository;


    @Override
    public StandardResponse signup(AuthRequestDto dto) {
        return null;
    }

    @Override
    @Transactional
    public StandardResponse authenticate(AuthRequestDto dto) {

        String email = "taechnique@yanolja.com";
        final VerifyInfo info = Optional.ofNullable(verifyInfoRepository.findByTarget(email)).orElseThrow(() ->
                new AuthorizingException(ServiceStatus.INVALID_PARAMETER));


        return DefaultResponse.ok();
    }
}
