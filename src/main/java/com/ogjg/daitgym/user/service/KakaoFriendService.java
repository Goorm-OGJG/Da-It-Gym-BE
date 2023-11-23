package com.ogjg.daitgym.user.service;

import com.ogjg.daitgym.common.exception.user.NotFoundUser;
import com.ogjg.daitgym.user.dto.response.KaKaoFriendsResponse;
import com.ogjg.daitgym.user.repository.UserAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KakaoFriendService {

    private final WebClient webClient;
    private final UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    public KakaoFriendService(WebClient.Builder webClientBuilder, UserAuthenticationRepository userAuthenticationRepository) {
        this.webClient = webClientBuilder.baseUrl("https://kapi.kakao.com").build();
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    /**
     * 카카오 서버로 accessToken을 담아 요청을 보내 친구목록을 받아오기
     *
     * @param email 로그인중인 사용자 이메일
     */
    @Transactional
    public Mono<KaKaoFriendsResponse> requestKaKaoFriendsList(
            String email
    ) {
        return webClient.get()
                .uri("/v1/api/talk/profile")
                .header("Authorization", "Bearer " + getAccessToken(email))
                .retrieve()
                .bodyToMono(KaKaoFriendsResponse.class);
    }

    private String getAccessToken(String email) {
        return userAuthenticationRepository.findByUserEmail(email)
                .orElseThrow(NotFoundUser::new)
                .getAccessToken();
    }

}
