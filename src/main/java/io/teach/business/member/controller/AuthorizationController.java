package io.teach.business.member.controller;

import io.teach.business.member.service.AuthService;
import io.teach.business.member.service.KakaoAuthService;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.business.auth.dto.request.AuthRequestDto;
import io.teach.infrastructure.http.body.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.teach.business.member.controller.AuthorizationController.BEAN_NAME;

@RestController(BEAN_NAME)
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AuthorizationController {

    public static final String BEAN_NAME = "authentication-provider";
    private AuthService authService;

    public void setDynamicAuthService(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<StandardResponse> signupForUser(@RequestBody @Valid final AuthRequestDto authDto) throws AuthorizingException {

        final StandardResponse response = authService.signup(authDto);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/auth")
    public ResponseEntity<StandardResponse> authorizeForUser(@RequestBody @Valid final AuthRequestDto authDto) throws AuthorizingException {

        final StandardResponse response = authService.authenticate(authDto);

        return ResponseEntity.ok(response);
    }
}
