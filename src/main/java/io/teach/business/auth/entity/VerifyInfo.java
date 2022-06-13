package io.teach.business.auth.entity;

import io.teach.business.auth.constant.VerifyStatus;
import io.teach.business.auth.constant.VerifyType;
import io.teach.infrastructure.util.RandomUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerifyInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String verifyTarget;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VerifyType verifyType;

    @Column(nullable = false)
    private String verifyNumber;

    @OneToMany(mappedBy = "verifyInfo", fetch = FetchType.LAZY)
    private List<AuthHistory> authHistory = new ArrayList<>();

    public static VerifyInfo createVerifyInfo(final String verifyTarget, final VerifyType verifyType, final Integer codeLength, final  AuthHistory authHistory) {
        final VerifyInfo verifyInfo = new VerifyInfo();

        verifyInfo.verifyTarget = verifyTarget;
        verifyInfo.verifyType = verifyType;
        verifyInfo.refreshVerifyToken(authHistory, codeLength);

        return verifyInfo;
    }

    public void refreshVerifyToken(final AuthHistory authHistory, final Integer codeLength) {
        this.authHistory.add(authHistory);
        this.verifyNumber = RandomUtil.randomNumeric(codeLength);
        authHistory.setVerifyInfo(this);
    }

    public Integer getTodayCount() {
        return this.getAuthHistory().stream()
                .filter(AuthHistory::wasItSentToday)
                .collect(Collectors.toList()).size();
    }


    public Integer getTodayRemainCount(final Integer todayMax) {
        return (todayMax - this.getTodayCount());
    }

    public void cancelOldHistories () {
        this.authHistory.stream().filter(h -> h.getVerifyStatus() == VerifyStatus.YET)
                .forEach(h -> h.changeStatus(VerifyStatus.CANCELED));
    }

    public Optional<AuthHistory> lastHistory() {
        return this.authHistory.stream().sorted((h1, h2) -> h2.getSendTime().compareTo(h1.getSendTime())).findFirst();
    }
}
