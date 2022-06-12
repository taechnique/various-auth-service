package io.teach.business.auth.controller;

import io.teach.business.auth.dto.SendEmailDto;
import io.teach.business.auth.dto.SendPhoneDto;
import io.teach.business.auth.dto.request.ConfirmEmailDto;
import io.teach.business.auth.service.CertifyService;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.http.body.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/infra")
public class InfraController {

    private final CertifyService certifyService;

    @PostMapping("/email/verify/send")
    public ResponseEntity<StandardResponse> sendVerifyNumberForEmail(
            @Valid
            @RequestBody
            final SendEmailDto reqDto) throws AuthorizingException {

        final StandardResponse response = certifyService.sendEmailForVerify(reqDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/email/verify/confirm")
    public ResponseEntity<StandardResponse> confirmEmailWithVerifyCode(
            @Valid
            @RequestBody
            final ConfirmEmailDto reqDto) throws AuthorizingException {

        final StandardResponse response = certifyService.confirmEmailForVerify(reqDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sms/certificate")
    public ResponseEntity<StandardResponse> sendCertifyNumberForPhone(
            @Valid
            @RequestBody
            final SendPhoneDto reqDto) throws AuthorizingException {

        StandardResponse response = certifyService.sendMessageForCertify(reqDto);

        return ResponseEntity.ok(response);
    }


}
