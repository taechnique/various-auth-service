package io.teach.business.auth.controller;


import io.teach.business.auth.service.CommonAuthService;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.http.body.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common/auth")
public class CommonAuthController {

    private final CommonAuthService service;

    @GetMapping("/check")
    public ResponseEntity<StandardResponse> checkDuplication(
            @RequestHeader("X-CHECK-TYPE")final String type,
            @RequestParam("info") final String info) throws AuthorizingException {

        StandardResponse response = service.checkDuplicationOfInfo(type, info);

        return ResponseEntity.ok(response);
    }
}
