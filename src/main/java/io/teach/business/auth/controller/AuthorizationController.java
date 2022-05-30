package io.teach.business.auth.controller;

import io.teach.business.auth.service.AuthService;
import io.teach.business.auth.service.AuthServiceFactory;
import io.teach.business.auth.strategy.AuthStrategy;
import io.teach.infrastructure.context.auth.AuthStrategyContextHolder;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.business.auth.dto.AuthRequestDto;
import io.teach.business.auth.dto.StandardAuthDto;
import io.teach.infrastructure.http.body.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static io.teach.business.auth.controller.AuthorizationController.BEAN_NAME;

@RestController(BEAN_NAME)
@RequestMapping("/api/v1/user")
public class AuthorizationController {

    public static final String BEAN_NAME = "authentication-provider";
    private AuthService authService;


    public AuthorizationController() {
        System.out.println("AuthorizationController::new");
    }

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
