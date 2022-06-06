package io.teach.business.auth.controller;


import io.teach.business.auth.dto.request.ValidateDto;
import io.teach.business.auth.service.MemberJoinService;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.http.body.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberJoinController {

    private final MemberJoinService service;

    @PostMapping("/join/validate")
    public ResponseEntity<StandardResponse> validate(@RequestBody @Valid final ValidateDto dto) throws AuthorizingException {

        final StandardResponse response = service.validateInJoin(dto);

        return ResponseEntity.ok(response);
    }
}
