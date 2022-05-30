package io.teach.infrastructure.service;

import io.teach.infrastructure.constant.MailType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final AuthMailSenderServiceAdapter mailSender;

}
