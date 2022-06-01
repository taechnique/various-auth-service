package io.teach.business.member.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class UserAuthInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccountInfo userAccount;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private LocalDateTime createTime;


}
