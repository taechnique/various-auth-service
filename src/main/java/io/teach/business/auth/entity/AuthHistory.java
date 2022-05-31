package io.teach.business.auth.entity;

import io.teach.business.auth.constant.AccountType;
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

    private String group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verify_info_id", nullable = false)
    private VerifyInfo verifyInfo;

    @Column(nullable = false)
    private VerifyType verifyType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime sendTime;

    private LocalDateTime verifyTime;

    public void setVerifyInfo(VerifyInfo verifyInfo) {
        this.verifyInfo = verifyInfo;
    }

    public Boolean isInToday

    public static AuthHistory createHistory(final String group, final VerifyType verifyType) {
        Assert.notNull(group, "Group cannot be null");
        Assert.notNull(verifyType, "verify type cannot be null.");

        final AuthHistory history = new AuthHistory();
        final HistoryGroup historyGroup = HistoryGroup.groupOf(group).orElseThrow(() ->
                new AuthorizingException(ServiceStatus.INVALID_PARAMETER));

        history.group = historyGroup.getGroup();
        history.verifyType = verifyType;
        history.description = verifyType.getDescription();
        history.sendTime = LocalDateTime.now();

        return history;
    }

}
