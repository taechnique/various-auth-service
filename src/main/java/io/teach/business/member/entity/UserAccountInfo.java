package io.teach.business.member.entity;

import io.teach.business.auth.constant.AccountMemberType;
import io.teach.business.auth.constant.AccountProviderType;
import io.teach.infrastructure.util.RandomUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccountInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountProviderType accountType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountMemberType memberType;

    @Column(nullable = false)
    private String hashedMemberNo;

    @Column(nullable = false)
    private String providerId;

    @Column(unique = true, nullable = false)
    private String email;

    private String nickName;

    @Column(nullable = false)
    private String passphrase;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String phoneYN;

    @OneToOne(mappedBy = "userAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserAuthInfo authInfo;

    private LocalDateTime createTime;

    public static UserAccountInfo create(
            final AccountProviderType accountType,
            final AccountMemberType memberType,
            final String email,
            final String passphrase,
            final String nickName,
            final String phone) {
        final UserAccountInfo info = new UserAccountInfo();
        info.accountType = accountType;
        info.memberType = memberType;
        info.hashedMemberNo = RandomUtil.randomString();
        info.providerId = RandomUtil.randomHexNumeric(32);
        info.email = email;
        info.passphrase = passphrase;
        info.nickName = nickName;
        info.phone = phone;
        info.phoneYN = "Y";
        info.createTime = LocalDateTime.now();

        return info;
    }

    public void setAuthInfo(final UserAuthInfo authInfo) {
        this.authInfo = authInfo;
    }
}
