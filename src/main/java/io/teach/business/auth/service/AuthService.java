package io.teach.business.auth.service;

import io.teach.business.auth.dto.AuthRequestDto;
import io.teach.infrastructure.http.body.StandardResponse;

public interface AuthService {

    StandardResponse authenticate(AuthRequestDto dto);

}
