package io.teach.business.auth.controller;

import io.teach.business.auth.service.AuthService;
import io.teach.business.auth.service.AuthServiceFactory;
import io.teach.business.auth.strategy.AuthStrategy;
import io.teach.infrastructure.context.auth.AuthStrategyContextHolder;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.business.auth.dto.AuthRequestDto;
import io.teach.business.auth.dto.StandardAuthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@ResponseBody
@RequestMapping("/api/v1/auth")
public class AuthorizationController {

    private AuthService authService;

    public void setDynamicAuthService(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<StandardAuthDto> authorizeForUser(@RequestBody @Valid final AuthRequestDto authDto) throws AuthorizingException {
        System.out.println("authService = " + authService);
        System.out.println("this = " + this);
        System.out.println("authDto: " + authDto);
        authService.authenticate();

        return ResponseEntity.ok(null);
    }
}
