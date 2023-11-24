package com.ogjg.daitgym.user.service;

import com.ogjg.daitgym.common.exception.user.ForbiddenKaKaoSocial;
import com.ogjg.daitgym.common.exception.user.NotFoundUser;
import com.ogjg.daitgym.common.exception.user.NotFoundUserAuthentication;
import com.ogjg.daitgym.domain.UserAuthentication;
import com.ogjg.daitgym.user.dto.response.KaKaoFriendsResponse;
import com.ogjg.daitgym.user.repository.UserAuthenticationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KakaoFriendService {

    private final RestTemplate restTemplate;
    private final UserAuthenticationRepository userAuthenticationRepository;

    public KakaoFriendService(UserAuthenticationRepository userAuthenticationRepository) {
        this.restTemplate = new RestTemplate();
        this.userAuthenticationRepository = userAuthenticationRepository;
    }


    /**
     * 카카오 서버로 accessToken을 담아 요청을 보내 친구목록을 받아오기
     *
     * @param email 로그인중인 사용자 이메일
     */
    @Transactional
    public KaKaoFriendsResponse requestKaKaoFriendsList(String email) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + getAccessToken(email));
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<KaKaoFriendsResponse> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v1/api/talk/friends",
                    HttpMethod.GET,
                    entity,
                    KaKaoFriendsResponse.class
            );

            KaKaoFriendsResponse kaKaoFriendsResponse = response.getBody();

            kaKaoFriendsResponse.getElements().forEach(
                    kaKaoFriendResponseDto -> {
                        UserAuthentication userAuthentication = userAuthenticationRepository.findByProviderId(kaKaoFriendResponseDto.getId())
                                .orElseThrow(NotFoundUserAuthentication::new);

                        kaKaoFriendResponseDto.putUserData(
                                userAuthentication.getUser().getNickname(),
                                userAuthentication.getUser().getImageUrl()
                        );
                    });

            return kaKaoFriendsResponse;
        } catch (HttpClientErrorException.Forbidden e) {
            log.error(e.getMessage());
            throw new ForbiddenKaKaoSocial();
        }
    }

    private String getAccessToken(String email) {
        return userAuthenticationRepository.findByUserEmail(email)
                .orElseThrow(NotFoundUser::new)
                .getAccessToken();
    }

}
