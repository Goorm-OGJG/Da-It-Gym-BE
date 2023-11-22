package com.ogjg.daitgym.user.service;

import com.ogjg.daitgym.common.exception.user.NotFoundUser;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserHelper {

    private final UserRepository userRepository;

    public User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(NotFoundUser::new);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundUser::new);
    }

    public boolean isUserNotFoundByEmail(String loginEmail) {
        return !userRepository.findByEmail(loginEmail).isPresent();
    }

    public boolean isNicknameAlreadyExist(String nickname, String newNickname) {
        return !nickname.equals(newNickname) && userRepository.findByNickname(newNickname).isPresent();
    }
}
