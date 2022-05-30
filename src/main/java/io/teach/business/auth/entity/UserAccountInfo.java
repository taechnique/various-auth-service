package io.teach.business.auth.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class UserAccountInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String passphrase;

    private String email;

    private String phone;

    @OneToOne(mappedBy = "userAccount", fetch = FetchType.LAZY)
    private UserAuthInfo authInfo;

    private LocalDateTime createTime;

}
