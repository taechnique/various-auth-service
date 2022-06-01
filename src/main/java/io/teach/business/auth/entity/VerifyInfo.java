package io.teach.business.auth.entity;

import io.teach.business.auth.constant.VerifyType;
import io.teach.infrastructure.util.RandomUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
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

    public static VerifyInfo createVerifyInfo(final String verifyTarget, final VerifyType verifyType, final Integer CodeLength, final  AuthHistory authHistory) {
        final VerifyInfo verifyInfo = new VerifyInfo();

        verifyInfo.verifyTarget = verifyTarget;
        verifyInfo.verifyType = verifyType;
        verifyInfo.verifyNumber = RandomUtil.randomNumeric(CodeLength);
        verifyInfo.authHistory.add(authHistory);
        authHistory.setVerifyInfo(verifyInfo);

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
}
