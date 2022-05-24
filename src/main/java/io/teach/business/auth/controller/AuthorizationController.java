package io.teach.business.auth.controller;

import io.teach.business.auth.service.AuthService;
import io.teach.business.auth.service.AuthServiceFactory;
import io.teach.business.auth.strategy.AuthStrategy;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.business.auth.dto.AuthRequestDto;
import io.teach.business.auth.dto.StandardAuthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("authentication-publisher")
@RequestMapping("/api/v1/auth")
public class AuthorizationController {

    private AuthService authService;

    @Autowired
    public AuthorizationController(final AuthServiceFactory authServiceFactory) {

        this.authService = authServiceFactory.create(AuthStrategy.DEFAULT);
    }

    @PostMapping
    public ResponseEntity<StandardAuthDto> authorizeForUser(@RequestBody @Validated AuthRequestDto authDto) throws AuthorizingException {

        System.out.println("authDto: " + authDto);
        authService.authenticate();

        return ResponseEntity.ok(null);
    }
}
