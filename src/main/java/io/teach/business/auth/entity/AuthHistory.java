package io.teach.business.auth.entity;

import io.teach.business.auth.constant.HistoryGroup;
import io.teach.business.auth.constant.VerifyStatus;
import io.teach.business.auth.constant.VerifyType;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.util.RandomUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "verify_info_id", nullable = false)
    private VerifyInfo verifyInfo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VerifyType verifyType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VerifyStatus verifyStatus;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String verifyPermitToken;

    @Column(nullable = false)
    private LocalDateTime sendTime;

    @Column(nullable = false)
    private LocalDateTime expiredTime;

    private LocalDateTime verifyTime;

    public void setVerifyInfo(final VerifyInfo verifyInfo) {

        this.verifyInfo = verifyInfo;
    }

    public static AuthHistory createHistory(final HistoryGroup historyGroup, final VerifyType verifyType, final int expiredSecond) {
        Assert.notNull(historyGroup, "Group cannot be null");
        Assert.notNull(verifyType, "verify type cannot be null.");

        final AuthHistory history = new AuthHistory();

        history.groupType = historyGroup.getGroup();
        history.verifyType = verifyType;
        history.description = verifyType.getDescription();
        history.verifyPermitToken = RandomUtil.randomToken();
        LocalDateTime now = LocalDateTime.now();
        history.sendTime = now;
        history.verifyStatus = VerifyStatus.YET;
        history.expiredTime = now.plusSeconds(expiredSecond);

        return history;
    }


    public Boolean wasItSentToday() {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime startTime = now.with(LocalTime.MIN);
        final LocalDateTime endTime = now.with(LocalTime.MAX);

        return (startTime.isBefore(this.getSendTime()) &&
                endTime.isAfter(this.getSendTime()));
    }

    public Boolean isExpired() {
        final LocalDateTime now = LocalDateTime.now();

        return now.isAfter(this.getExpiredTime());
    }

    public void verify(final String code) {
        if(isExpired())
            throw new AuthorizingException(ServiceStatus.ALREADY_EXPIRED_HISTORY);

        final VerifyInfo verifyInfo = this.getVerifyInfo();

        if( ! verifyInfo.getVerifyNumber().equals(code))
            throw new AuthorizingException(ServiceStatus.INVALID_VERIFY_NUMBER);

        if(this.getVerifyStatus() != VerifyStatus.YET)
            throw new AuthorizingException(ServiceStatus.ALREADY_PROCESSED_HISTORY);

        this.verifyTime = LocalDateTime.now();
        this.verifyStatus = VerifyStatus.VERIFIED;
    }

    public void changeStatus(final VerifyStatus status) {
        //== null safe ==//
        if(status != VerifyStatus.VERIFIED && status != VerifyStatus.CANCELED)
            throw new AuthorizingException(ServiceStatus.ONLY_CHANGE_TO_CANCELED_OR_VERIFIED);

        this.verifyStatus = status;
    }
}
