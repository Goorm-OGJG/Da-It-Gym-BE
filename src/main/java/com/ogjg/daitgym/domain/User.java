package com.ogjg.daitgym.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE email = ?")
@Where(clause = "is_deleted = false")
public class User extends BaseEntity {

    @Id
    @Email
    private String email;

    private String password;

    private String nickname;

    private LocalDate birth;

    @Column(length = 11)
    private String phoneNumber;

    private String intro;

    private String imageUrl;

    @Enumerated(STRING)
    private Role role;

    private boolean isDeleted;

    // 적용중인 루틴
}



