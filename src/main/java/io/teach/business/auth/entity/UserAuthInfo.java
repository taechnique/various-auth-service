package io.teach.business.auth.entity;

import io.teach.business.auth.constant.AccountType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class UserAuthInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccountInfo userAccount;

    @Column(nullable = false)
    private AccountType accountType;

    @Column(nullable = false)
    private String accessToken;

    private LocalDateTime createTime;


}
