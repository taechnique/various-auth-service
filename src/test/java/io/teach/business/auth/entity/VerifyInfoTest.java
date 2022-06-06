package io.teach.business.auth.entity;

import io.teach.business.auth.constant.HistoryGroup;
import io.teach.business.auth.constant.VerifyStatus;
import io.teach.business.auth.constant.VerifyType;
import io.teach.infrastructure.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class VerifyInfoTest {


    private AuthHistory authHistory;

    @BeforeEach
    void init() {
        this.authHistory = AuthHistory.createHistory(HistoryGroup.JOIN, VerifyType.EMAIL, 600);
    }

    @Test
    @DisplayName("[인증정보 생성] 정상 생성")
    public void createVerifyInfo () throws Throwable {
        try (MockedStatic<RandomUtil> randomUtil = mockStatic(RandomUtil.class)) {
            /* Given */
            final String email = "taechnique@yanolja.com";
            final int codeLength = 6;
            final String verifyCode = "123456";

            /* When */
            when(RandomUtil.randomNumeric(codeLength))
                    .thenReturn(verifyCode);
            final VerifyInfo verifyInfo = VerifyInfo.createVerifyInfo(email, VerifyType.EMAIL, codeLength, authHistory);

            /* Then */
            assertEquals(verifyCode, verifyInfo.getVerifyNumber());
            assertEquals(authHistory, verifyInfo.getAuthHistory().get(0));
        }
    }

    @Test
    @DisplayName("[인증 정보 토큰 초기회] 정상 초기화")
    public void refreshVerifyToken() throws Throwable {
        try (MockedStatic<RandomUtil> randomUtil = mockStatic(RandomUtil.class)) {
            /* Given */
            final String email = "taechnique@yanolja.com";
            final int codeLength = 6;
            final String verifyCode = "123456";
            final String refreshCode = "654321";

            /* When */
            final VerifyInfo verifyInfo = VerifyInfo.createVerifyInfo(email, VerifyType.EMAIL, codeLength, authHistory);
            AuthHistory other = AuthHistory.createHistory(HistoryGroup.JOIN, VerifyType.EMAIL, 600);
            when(RandomUtil.randomNumeric(codeLength))
                    .thenReturn(refreshCode);
            verifyInfo.refreshVerifyToken(other, codeLength);

            /* Then */
            assertEquals(refreshCode, verifyInfo.getVerifyNumber());
            assertEquals(other, verifyInfo.getAuthHistory().get(1));
        }

    }

    @Test
    @DisplayName("[오늘보낸 요청확인] 인증요청 개수 확인")
    public void getTodayCount() throws Throwable {
        final LocalDateTime now = LocalDateTime.now();
        try (MockedStatic<LocalDateTime> localDateTime = mockStatic(LocalDateTime.class)) {
            /* Given */
            final String email = "taechnique@yanolja.com";
            final int codeLength = 6;

            /* When */
            when(LocalDateTime.now())
                    .thenReturn(now.minusDays(1));
            AuthHistory history1 = AuthHistory.createHistory(HistoryGroup.JOIN, VerifyType.EMAIL, 600);
            VerifyInfo verifyInfo = VerifyInfo.createVerifyInfo(email, VerifyType.EMAIL, codeLength, history1);

            //== Today ==//
            when(LocalDateTime.now())
                    .thenReturn(now);
            AuthHistory today1 = AuthHistory.createHistory(HistoryGroup.JOIN, VerifyType.EMAIL, 600);
            verifyInfo.refreshVerifyToken(today1, codeLength);

            when(LocalDateTime.now())
                    .thenReturn(now.minusDays(1));
            AuthHistory history2 = AuthHistory.createHistory(HistoryGroup.JOIN, VerifyType.EMAIL, 600);
            verifyInfo.refreshVerifyToken(history2, codeLength);

            //== Today ==//
            when(LocalDateTime.now())
                    .thenReturn(now);
            AuthHistory today2 = AuthHistory.createHistory(HistoryGroup.JOIN, VerifyType.EMAIL, 600);
            verifyInfo.refreshVerifyToken(today2, codeLength);


            /* Then */
            assertEquals(2, verifyInfo.getTodayCount());
        }
    }

    @Test
    @DisplayName("[오늘 인증가능 횟수 확인] 인증가능 횟수 확인")
    public void getTodayRemainCount() throws Throwable {
        final LocalDateTime now = LocalDateTime.now();
        try (MockedStatic<LocalDateTime> localDateTime = mockStatic(LocalDateTime.class)) {
            /* Given */
            final String email = "taechnique@yanolja.com";
            final int codeLength = 6;
            final int todayMax = 5;

            /* When */
            when(LocalDateTime.now())
                    .thenReturn(now.minusDays(1));
            AuthHistory history1 = AuthHistory.createHistory(HistoryGroup.JOIN, VerifyType.EMAIL, 600);
            VerifyInfo verifyInfo = VerifyInfo.createVerifyInfo(email, VerifyType.EMAIL, codeLength, history1);

            //== Today ==//
            when(LocalDateTime.now())
                    .thenReturn(now);
            AuthHistory today1 = AuthHistory.createHistory(HistoryGroup.JOIN, VerifyType.EMAIL, 600);
            verifyInfo.refreshVerifyToken(today1, codeLength);

            when(LocalDateTime.now())
                    .thenReturn(now.minusDays(1));
            AuthHistory history2 = AuthHistory.createHistory(HistoryGroup.JOIN, VerifyType.EMAIL, 600);
            verifyInfo.refreshVerifyToken(history2, codeLength);

            //== Today ==//
            when(LocalDateTime.now())
                    .thenReturn(now);
            AuthHistory today2 = AuthHistory.createHistory(HistoryGroup.JOIN, VerifyType.EMAIL, 600);
            verifyInfo.refreshVerifyToken(today2, codeLength);


            /* Then */
            assertEquals(3, verifyInfo.getTodayRemainCount(todayMax));
        }
    }

    @Test
    @DisplayName("[이전 요청 상태변경] 이전 인증 요청이력 취소 처리")
    public void cancelOldHistories() throws Throwable {
        /* Given */
        final String email = "taechnique@yanolja.com";
        final int codeLength = 6;

        /* When */
        AuthHistory history = AuthHistory.createHistory(HistoryGroup.JOIN, VerifyType.EMAIL, 600);
        VerifyInfo verifyInfo = VerifyInfo.createVerifyInfo(email, VerifyType.EMAIL, codeLength, history);
        verifyInfo.cancelOldHistories();

        /* Then */
        assertEquals(VerifyStatus.CANCELED, history.getVerifyStatus());
    }


}