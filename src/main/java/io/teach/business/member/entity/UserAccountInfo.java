package io.teach.business.member.entity;

import io.teach.business.auth.constant.AccountProviderType;
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

    @Column(unique = true)
    private String loginId;

    private String username;

    @Column(unique = true)
    private String passphrase;

    private String email;

    private String phone;

    @OneToOne(mappedBy = "userAccount", fetch = FetchType.LAZY)
    private UserAuthInfo authInfo;

    private LocalDateTime createTime;

    public static UserAccountInfo create(final String loginId, final String passphrase) {
        UserAccountInfo info = new UserAccountInfo();

        info.loginId = loginId;
        info.passphrase = passphrase;

        return info;
    }
}
