package io.teach.business.member.service;

import io.teach.business.auth.dto.request.AuthRequestDto;
import io.teach.infrastructure.http.body.StandardResponse;

public interface AuthService {

    StandardResponse authenticate(AuthRequestDto dto);

    StandardResponse signup(AuthRequestDto dto);
}
