package io.teach.infrastructure.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncEmailTransferService {


    @Async
    public void sendEmailAsAsync(final String toAddress, final String verifyCode) {
        final String threadName = Thread.currentThread().getName();
        System.out.printf("%s - [야놀자][수신: %s] 이메일 인증을 위한 인증번호가 발급 되었습니다. [%s]\n", threadName, toAddress, verifyCode);

    }
}
