package io.teach.user.controller;

import io.teach.business.user.dto.AuthRequestDto;
import io.teach.business.user.dto.StandardAuthDto;
import io.teach.infrastructure.excepted.AuthorizingException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/auth")
public class AuthorizationController {

    @PostMapping
    public ResponseEntity<StandardAuthDto> authorizeForUser(@RequestBody @Validated AuthRequestDto authDto) throws AuthorizingException {

        System.out.println("authDto = " + authDto);
        return ResponseEntity.ok(null);
    }
}
