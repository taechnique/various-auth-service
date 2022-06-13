package io.teach.business.member.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuthInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id")
    private UserAccountInfo userAccount;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    public static UserAuthInfo create(final UserAccountInfo accountInfo) {
        final UserAuthInfo authInfo = new UserAuthInfo();
        authInfo.setAccountInfo(accountInfo);
        authInfo.accessToken = "";
        authInfo.updateTime = LocalDateTime.now();
        return authInfo;
    }

    public void refresh(final String token) {
        this.accessToken = token;
        this.updateTime = LocalDateTime.now();
    }

    public void setAccountInfo(final UserAccountInfo accountInfo) {
        this.userAccount = accountInfo;
        accountInfo.setAuthInfo(this);
    }
}
