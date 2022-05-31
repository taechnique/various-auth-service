package io.teach.business.auth.entity;

import io.teach.business.auth.constant.AccountProviderType;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class UserAccountInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private AccountProviderType accountType;

    @Column(unique = true)
    private String loginId;

    private String username;

    @Column(unique = true)
    private String passphrase;

    @Column(unique = true)
    private String email;

    private String phone;

    @OneToOne(mappedBy = "userAccount", fetch = FetchType.LAZY)
    private UserAuthInfo authInfo;

    private LocalDateTime createTime;

}
