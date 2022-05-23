package io.teach.user.controller;

import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.user.dto.AuthRequestDto;
import io.teach.user.dto.StandardAuthDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/auth")
public class AuthorizationController {

    @PostMapping
    public ResponseEntity<StandardAuthDto> authorizeForUser(@RequestBody @Validated AuthRequestDto authDto) throws AuthorizingException {


        return null;
    }
}
