package io.teach.business.auth.entity;

import io.teach.business.auth.constant.HistoryGroup;
import io.teach.business.auth.constant.VerifyType;
import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthHistory {

    @Id @GeneratedValue
    private Long id;

    private String groupType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "verify_info_id", nullable = false)
    private VerifyInfo verifyInfo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VerifyType verifyType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime sendTime;

    @Column(nullable = false)
    private LocalDateTime expiredTime;

    private LocalDateTime verifyTime;

    public void setVerifyInfo(final VerifyInfo verifyInfo) {

        this.verifyInfo = verifyInfo;
    }

    public Boolean wasItSentToday () {
        final LocalDateTime now = LocalDateTime.now();

        return (now.with(LocalDateTime.MIN).isBefore(this.sendTime) &&
                now.with(LocalDateTime.MAX).isAfter(this.sendTime));
    }

    public static AuthHistory createHistory(final String group, final VerifyType verifyType, final int expiredMinute) {
        Assert.notNull(group, "Group cannot be null");
        Assert.notNull(verifyType, "verify type cannot be null.");

        final AuthHistory history = new AuthHistory();
        final HistoryGroup historyGroup = HistoryGroup.groupOf(group).orElseThrow(() ->
                new AuthorizingException(ServiceStatus.INVALID_PARAMETER));

        history.groupType = historyGroup.getGroup();
        history.verifyType = verifyType;
        history.description = verifyType.getDescription();
        LocalDateTime now = LocalDateTime.now();
        history.sendTime = now;
        history.expiredTime = now.plusMinutes(expiredMinute);

        return history;
    }

}
