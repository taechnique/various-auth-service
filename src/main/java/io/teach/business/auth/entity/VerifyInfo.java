package io.teach.business.auth.entity;

import io.teach.business.auth.constant.VerifyType;
import io.teach.infrastructure.util.RandomUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerifyInfo {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String verifyTarget;

    @Column(nullable = false)
    private VerifyType verifyType;

    @Column(nullable = false)
    private String verifyNumber;

    @OneToMany(mappedBy = "verifyInfo")
    private List<AuthHistory> authHistory;

    public static VerifyInfo createVerifyInfo(final String verifyTarget, final VerifyType verifyType) {
        final VerifyInfo verifyInfo = new VerifyInfo();

        verifyInfo.verifyTarget = verifyTarget;
        verifyInfo.verifyType = verifyType;
        verifyInfo.verifyNumber = RandomUtil.randomNumeric(6);

        return verifyInfo;
    }

    public void addAuthHistory(final AuthHistory authHistory) {
        this.authHistory.add(authHistory);
        authHistory.setVerifyInfo(this);
    }
}
