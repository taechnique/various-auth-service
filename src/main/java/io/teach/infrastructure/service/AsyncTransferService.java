package io.teach.infrastructure.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncTransferService {


    @Async
    public void sendEmailAsAsync(final String toAddress, final String verifyCode) {
        final String threadName = Thread.currentThread().getName();
        log.info("{} - [야놀자][수신: {}] 이메일 인증을 위한 인증번호가 발급 되었습니다. [{}]", threadName, toAddress, verifyCode);

    }

    @Async
    public void sendMessageAsAsync(final String digits, final String verifyCode) {
        final String threadName = Thread.currentThread().getName();
        log.info("[야놀자] 인증번호 [{}]를 입력해주세요.", verifyCode);
    }
}
