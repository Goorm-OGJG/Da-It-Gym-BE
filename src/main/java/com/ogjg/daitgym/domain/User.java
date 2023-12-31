package com.ogjg.daitgym.domain;

import com.ogjg.daitgym.domain.routine.Routine;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "health_" +
            "club_id")
    private HealthClub healthClub;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "active_routine_id")
    private Routine activeRoutine;

    @Pattern(regexp = "^[a-zA-Z0-9_]{3,36}$", message = "닉네임은 영문, 숫자, _ 만 사용 가능하며, 길이는 3자 이상 36자 이하여야 합니다.")
    @Column(unique = true)
    private String nickname;

    private LocalDate birth;

    @Column(length = 11)
    private String phoneNumber;

    private String introduction;

    private String imageUrl;

    @Enumerated(STRING)
    private Role role;

    @Enumerated(STRING)
    private ExerciseSplit preferredSplit;

    private boolean isDeleted;

    @Builder
    public User(String email, HealthClub healthClub, Routine activeRoutine, String nickname, LocalDate birth, String phoneNumber, String introduction, String imageUrl, Role role, ExerciseSplit preferredSplit, boolean isDeleted) {
        this.email = email;
        this.healthClub = healthClub;
        this.activeRoutine = activeRoutine;
        this.nickname = nickname;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.imageUrl = imageUrl;
        this.role = role;
        this.preferredSplit = preferredSplit;
        this.isDeleted = isDeleted;
    }

    public void editProfile(String newImgUrl, String nickname, String introduction, HealthClub healthClub, String split) {
        this.imageUrl = newImgUrl;
        changeNickname(nickname);
        this.introduction = introduction;
        changeHealthClub(healthClub);
        this.preferredSplit = ExerciseSplit.titleFrom(split);
    }

    public String changeNickname(String newNickname) {
        return this.nickname = newNickname;
    }

    public void withdraw() {
        this.isDeleted = true;
    }

    public void changeHealthClub(HealthClub newHealthClub) {
        if (this.healthClub != null) {
            this.healthClub.getUsers().remove(this);
        }

        if (newHealthClub != null && newHealthClub.getUsers() != null && !newHealthClub.getUsers().contains(this)) {
            newHealthClub.getUsers().add(this);
        }
        this.healthClub = newHealthClub;
    }

    public void promoteToTrainer() {
        if (this.role == Role.USER) {
            this.role = Role.TRAINER;
        }
    }
}
