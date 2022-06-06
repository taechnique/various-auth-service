package io.teach.business.auth.entity;

import io.teach.business.auth.constant.HistoryGroup;
import io.teach.business.auth.constant.VerifyStatus;
import io.teach.business.auth.constant.VerifyType;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.util.RandomUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class AuthHistoryTest {

    @Test
    @DisplayName("[인증요청 이력 생성] 정상 생성")
    public void createHistory() throws Throwable {

        final LocalDateTime now = LocalDateTime.now();
        try(
                final MockedStatic<RandomUtil> randomUtil = mockStatic(RandomUtil.class);
                final MockedStatic<LocalDateTime> localDateTime = mockStatic(LocalDateTime.class)
                ) {

            /* Given */
            final HistoryGroup historyGroup = HistoryGroup.JOIN;
            final VerifyType verifyType = VerifyType.EMAIL;
            final String token = "4ed179f6-9703-4a64-b7c3-b27ca92ff0d1";
            final int expiredSecond = 600;

            /* When */
            when(RandomUtil.randomToken())
                    .thenReturn(token);
            when(LocalDateTime.now())
                    .thenReturn(now);
            final AuthHistory history = AuthHistory.createHistory(historyGroup, verifyType, expiredSecond);

            /* Then */
            assertEquals(historyGroup.getGroup(), history.getGroupType());
            assertEquals(verifyType, history.getVerifyType());
            assertEquals(verifyType.getDescription(), history.getDescription());
            assertEquals(token, history.getVerifyPermitToken());
            assertEquals(now, history.getSendTime());
            assertEquals(VerifyStatus.YET, history.getVerifyStatus());
            assertEquals(now.plusSeconds(expiredSecond), history.getExpiredTime());

        }
    }

    @Test
    @DisplayName("[인증요청 이력 생성] 이력 그룹이 Null")
    public void createHistory1() throws Throwable {
        /* Given */
        final HistoryGroup historyGroup = null;
        final VerifyType verifyType = VerifyType.EMAIL;

        /* When */
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class, () -> AuthHistory.createHistory(historyGroup, verifyType, 600));

        /* Then */
        assertEquals(IllegalArgumentException.class, actual.getClass());

    }

    @Test
    @DisplayName("[인증요청 이력 생성] 인증 타입이 Null")
    public void createHistory2() throws Throwable {
        /* Given */
        final HistoryGroup historyGroup = HistoryGroup.JOIN;
        final VerifyType verifyType = null;

        /* When */
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class, () -> AuthHistory.createHistory(historyGroup, verifyType, 600));

        /* Then */
        assertEquals(IllegalArgumentException.class, actual.getClass());
    }

    @Test
    @DisplayName("[작성일 검증] 오늘 작성한 경우")
    public void wasItSentToday() throws Throwable {
        /* Given */
        final HistoryGroup historyGroup = HistoryGroup.JOIN;
        final VerifyType verifyType = VerifyType.EMAIL;
        final int expiredSecond = 600;

        /* When */
        final AuthHistory history = AuthHistory.createHistory(historyGroup, verifyType, expiredSecond);

        /* Then */
        assertTrue(history.wasItSentToday());
    }

    @Test
    @DisplayName("[작성일시 검증] 오늘 작성하지 않은 경우")
    public void wasItSentToday1() throws Throwable {
        LocalDateTime now = LocalDateTime.now();
        try (MockedStatic<LocalDateTime> localDatetime = mockStatic(LocalDateTime.class)) {

            /* Given */
            final HistoryGroup historyGroup = HistoryGroup.JOIN;
            final VerifyType verifyType = VerifyType.EMAIL;
            final int expiredSecond = 600;

            /* When */
            when(LocalDateTime.now())
                    .thenReturn(now.minusDays(1));
            final AuthHistory history = AuthHistory.createHistory(historyGroup, verifyType, expiredSecond);
            when(LocalDateTime.now())
                    .thenReturn(now);

            /* Then */
            assertFalse(history.wasItSentToday());
        }

    }
    @Test
    @DisplayName("[만료일시 검증] 만료되지 않은 경우")
    public void isExpired() throws Throwable {
        /* Given */
        final HistoryGroup historyGroup = HistoryGroup.JOIN;
        final VerifyType verifyType = VerifyType.EMAIL;
        final int expiredSecond = 600;

        /* When */
        final AuthHistory history = AuthHistory.createHistory(historyGroup, verifyType, expiredSecond);

        /* Then */
        assertFalse(history.isExpired());

    }

    @Test
    @DisplayName("[만료일시 검증] 만료된 경우")
    public void isExpired1() throws Throwable {
        LocalDateTime now = LocalDateTime.now();
        try (MockedStatic<LocalDateTime> localDatetime = mockStatic(LocalDateTime.class)) {
            /* Given */
            final HistoryGroup historyGroup = HistoryGroup.JOIN;
            final VerifyType verifyType = VerifyType.EMAIL;
            final int expiredSecond = 600;

            /* When */
            when(LocalDateTime.now())
                    .thenReturn(now.minusSeconds(expiredSecond + 60));
            final AuthHistory history = AuthHistory.createHistory(historyGroup, verifyType, expiredSecond);
            when(LocalDateTime.now())
                    .thenReturn(now);

            /* Then */
            assertTrue(history.isExpired());

        }
    }

    @Test
    @DisplayName("[인증번호 검증] 정상 인증완료")
    public void verify() throws Throwable {
        try(MockedStatic<RandomUtil> randomUtil = mockStatic(RandomUtil.class)) {
            /* Given */
            final String email = "taechnique@yanolja.com";
            final HistoryGroup historyGroup = HistoryGroup.JOIN;
            final VerifyType verifyType = VerifyType.EMAIL;
            final int expiredSecond = 600;
            final int codeLength = 6;
            final String verifyCode = "123456";

            /* When */
            final AuthHistory history = AuthHistory.createHistory(historyGroup, verifyType, expiredSecond);
            when(RandomUtil.randomNumeric(6))
                    .thenReturn(verifyCode);
            VerifyInfo verifyInfo = VerifyInfo.createVerifyInfo(email, VerifyType.EMAIL, codeLength, history);

            /* Then */
            assertDoesNotThrow(() -> history.verify(verifyCode));
            assertEquals(VerifyStatus.VERIFIED, history.getVerifyStatus());
        }
    }

    @Test
    @DisplayName("[인증번호 검증] 만료된 인증 요청")
    public void verify1() throws Throwable {
        final LocalDateTime now = LocalDateTime.now();
        try(MockedStatic<LocalDateTime> localDateTime = mockStatic(LocalDateTime.class)) {
            /* Given */
            final HistoryGroup historyGroup = HistoryGroup.JOIN;
            final VerifyType verifyType = VerifyType.EMAIL;
            final int expiredSecond = 600;
            final String verifyCode = "123456";

            /* When */
            when(LocalDateTime.now())
                    .thenReturn(now.minusSeconds(expiredSecond + 60));
            final AuthHistory history = AuthHistory.createHistory(historyGroup, verifyType, expiredSecond);
            when(LocalDateTime.now())
                    .thenReturn(now);
            final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> history.verify(verifyCode));

            /* Then */
            assertEquals(ServiceStatus.ALREADY_EXPIRED_HISTORY.getResCode(), actual.getServiceError().getResCode());
        }
    }

    @Test
    @DisplayName("[인증번호 검증] 일치하지 않는 인증번호")
    public void verify2() throws Throwable {
        try(MockedStatic<RandomUtil> randomUtil = mockStatic(RandomUtil.class)) {
            /* Given */
            final String email = "taechnique@yanolja.com";
            final HistoryGroup historyGroup = HistoryGroup.JOIN;
            final VerifyType verifyType = VerifyType.EMAIL;
            final int expiredSecond = 600;
            final String verifyCode = "123456";
            final int codeLength = 6;

            /* When */
            final AuthHistory history = AuthHistory.createHistory(historyGroup, verifyType, expiredSecond);
            when(RandomUtil.randomNumeric(6))
                    .thenReturn("654321");
            VerifyInfo.createVerifyInfo(email, VerifyType.EMAIL, codeLength, history);
            final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> history.verify(verifyCode));

            /* Then */
            assertEquals(ServiceStatus.INVALID_VERIFY_NUMBER.getResCode(), actual.getServiceError().getResCode());
        }
    }

    @Test
    @DisplayName("[인증번호 검증] 올바르지 않은 이력상태")
    public void verify3() throws Throwable {
        try (MockedStatic<RandomUtil> randomUtil = mockStatic(RandomUtil.class)) {
            /* Given */
            final String email = "taechnique@yanolja.com";
            final HistoryGroup historyGroup = HistoryGroup.JOIN;
            final VerifyType verifyType = VerifyType.EMAIL;
            final int expiredSecond = 600;
            final String verifyCode = "123456";
            final int codeLength = 6;

            /* When */
            final AuthHistory history = AuthHistory.createHistory(historyGroup, verifyType, expiredSecond);
            when(RandomUtil.randomNumeric(6))
                    .thenReturn(verifyCode);
            VerifyInfo.createVerifyInfo(email, VerifyType.EMAIL, codeLength, history);
            history.changeStatus(VerifyStatus.CANCELED);
            final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> history.verify(verifyCode));

            /* Then */
            assertEquals(ServiceStatus.ALREADY_PROCESSED_HISTORY.getResCode(), actual.getServiceError().getResCode());
        }
    }

    @Test
    @DisplayName("[인증번호 검증] 잘못된 이력 상태 변경")
    public void changeStatus() throws Throwable {
        /* Given */
        final HistoryGroup historyGroup = HistoryGroup.JOIN;
        final VerifyType verifyType = VerifyType.EMAIL;
        final int expiredSecond = 600;

        /* When */
        final AuthHistory history = AuthHistory.createHistory(historyGroup, verifyType, expiredSecond);

        /* Then */
        assertDoesNotThrow(() -> history.changeStatus(VerifyStatus.CANCELED));
    }

    @Test
    @DisplayName("[인증번호 검증] 잘못된 이력 상태 변경")
    public void changeStatus1() throws Throwable {
        /* Given */
        final HistoryGroup historyGroup = HistoryGroup.JOIN;
        final VerifyType verifyType = VerifyType.EMAIL;
        final int expiredSecond = 600;

        /* When */
        final AuthHistory history = AuthHistory.createHistory(historyGroup, verifyType, expiredSecond);
        final AuthorizingException actual = assertThrows(AuthorizingException.class, () -> history.changeStatus(VerifyStatus.YET));

        /* Then */
        assertEquals(ServiceStatus.ONLY_CHANGE_TO_CANCELED_OR_VERIFIED.getResCode(), actual.getServiceError().getResCode());
    }


}