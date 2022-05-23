package io.teach.business.user.controller;

import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.business.user.dto.AuthRequestDto;
import io.teach.business.user.dto.StandardAuthDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("authentication-publisher")
@RequestMapping("/api/v1/auth")
public class AuthorizationController {

    @PostMapping
    public ResponseEntity<StandardAuthDto> authorizeForUser(@RequestBody @Valid AuthRequestDto authDto) throws AuthorizingException {

        System.out.println("called By AuthorizationController - authDto = " + authDto);

        return ResponseEntity.ok(null);
    }
}
