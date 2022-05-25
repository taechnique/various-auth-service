package io.teach.business.auth.entity;

import io.teach.business.auth.dto.model.UserAccountModel;
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
    private String username;

    @Column(nullable = false)
    private String passphrase;

    @OneToOne(mappedBy = "userAccount", fetch = FetchType.LAZY)
    private UserAuthInfo authInfo;

    private LocalDateTime createTime;

}
