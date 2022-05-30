package io.teach.infrastructure.service;

import io.teach.infrastructure.constant.MailType;

public interface AuthMailSenderServiceAdapter {

    void sendAuthMail(String toAddress, String number);
}
