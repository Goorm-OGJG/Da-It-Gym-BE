package com.ogjg.daitgym.user.service;

import com.ogjg.daitgym.approval.repository.*;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.WrongApproach;
import com.ogjg.daitgym.domain.*;
import com.ogjg.daitgym.follow.repository.FollowRepository;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalRepository;
import com.ogjg.daitgym.user.dto.request.ApplyForApprovalRequest;
import com.ogjg.daitgym.user.dto.request.EditUserProfileRequest;
import com.ogjg.daitgym.user.dto.request.RegisterInbodyRequest;
import com.ogjg.daitgym.user.dto.response.GetInbodiesResponse;
import com.ogjg.daitgym.user.dto.response.GetUserProfileGetResponse;
import com.ogjg.daitgym.user.exception.NotFoundUser;
import com.ogjg.daitgym.user.repository.HealthClubRepository;
import com.ogjg.daitgym.user.repository.InbodyRepository;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.user.dto.request.EditNicknameRequest;
import com.ogjg.daitgym.user.dto.response.EditInitialNicknameResponse;
import com.ogjg.daitgym.user.exception.AlreadyExistNickname;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final HealthClubRepository healthClubRepository;

    private final CertificationRepository certificationRepository;

    private final AwardRepository awardRepository;

    private final AwardImageRepository awardImageRepository;

    private final CertificationImageRepository certificationImageRepository;

    private final FollowRepository followRepository;

    private final ApprovalRepository approvalRepository;

    private final InbodyRepository inbodyRepository;

    private final ExerciseJournalRepository exerciseJournalRepository;

    @Transactional(readOnly = true)
    public GetUserProfileGetResponse getUserProfile(String loginEmail, String nickname) {
        User user = findUserByNickname(nickname);

        return GetUserProfileGetResponse.builder()
                .healthClubName(user.getHealthClub().getName())
                .journalCount(exerciseJournalRepository.countByUserEmail(user.getEmail()))
                .followerCount(followRepository.countByFollowPKTargetEmail(user.getEmail()))
                .followingCount(followRepository.countByFollowPKFollowerEmail(user.getEmail()))
                .build();
    }

    @Transactional
    public void editUserProfile(String loginEmail, String nickname, MultipartFile multipartFile, EditUserProfileRequest request) {
        User user = findUserByNickname(nickname);

        if (!loginEmail.equals(user.getEmail())) {
            throw new WrongApproach("본인의 프로필만 수정할 수 있습니다.");
        }

        // todo : s3에 이미지 저장
//        MultipartFile multipartFile;

        String awsImageUrl = "image-aws.com";

        //todo 헬스장 조회 수정, 수정 시 헬스장 식별자를 추가로 받아와야한다.
        HealthClub healthClub = findOrUpdateHealthClub(request.getGymName());

        user.editProfile(
                awsImageUrl,
                request.getIntroduction(),
                healthClub,
                request.getPreferredSplit()
        );
    }

    private HealthClub findOrUpdateHealthClub(String name) {
        HealthClub healthClub = healthClubRepository.findByName(name)
                .stream().findAny()
                .orElse(
                        HealthClub.builder()
                                .name(name)
                                .build()
                );

        return healthClubRepository.save(healthClub);
    }

    @Transactional
    public void applyForApproval(String loginEmail, ApplyForApprovalRequest request, List<MultipartFile> awardImgs, List<MultipartFile> certificationImgs) {
        User user = findUserByEmail(loginEmail);
        // todo : s3에 파일들 저장 및 url 반환
        List<String> awardImgUrls = List.of("awsUrl4.com","awsUrl5.com","awsUrl6.com");
        List<String> certificationImgUrls = List.of("awsUrl1.com","awsUrl2.com","awsUrl3.com");

        Approval savedApproval = approvalRepository.save(Approval.builder().build());

        List<Award> awards = awardRepository.saveAll(toAwards(user, request, awardImgUrls, savedApproval));
        awards.stream()
                .forEach(award -> awardImageRepository.saveAll(award.getAwardImages()));

        List<Certification> certifications = certificationRepository.saveAll(toCertifications(user, request, certificationImgUrls, savedApproval));
        certifications.stream()
                .forEach(certification -> certificationImageRepository.saveAll(certification.getCertificationImages()));
    }

    private List<Award> toAwards(User user, ApplyForApprovalRequest request, List<String> awardImgUrls, Approval approval) {
        return request.getAwards().stream()
                .map(awardsDto -> awardsDto.toAward(user, awardImgUrls, approval))
                .toList();
    }

    private List<Certification> toCertifications(User user, ApplyForApprovalRequest request, List<String> certificationImgUrls, Approval approval) {
        return request.getCertifications().stream()
                .map(certificationDto -> certificationDto.toCertification(user, certificationImgUrls, approval))
                .toList();
    }

    @Transactional
    public void registerInbody(String loginEmail, RegisterInbodyRequest request) {
        User user = findUserByEmail(loginEmail);

        Inbody inbody = Inbody.builder()
                .user(user)
                .measureAt(request.getMeasureAt())
                .score(request.getInbodyScore())
                .skeletalMuscleMass(request.getSkeletalMuscleMass())
                .bodyFatRatio(request.getBodyFatRatio())
                .weight(request.getWeight())
                .basalMetabolicRate(request.getBasalMetabolicRate())
                .build();

        inbodyRepository.save(inbody);
    }

    @Transactional(readOnly = true)
    public GetInbodiesResponse getInbodies(String nickname) {
        User user = findUserByNickname(nickname);

        return new GetInbodiesResponse(
                inbodyRepository.findByUserEmail(user.getEmail())
        );
    }

    public ResponseCookie getExpiredResponseCookie() {
        return ResponseCookie.from("refreshToken", null)
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();
    }

    @Transactional
    public EditInitialNicknameResponse editInitialNickname(String loginEmail, EditNicknameRequest request) {
        String newNickname = request.getNickname();

        if (isNicknameAlreadyExist(newNickname)) {
            throw new AlreadyExistNickname();
        }

        // 해당 메시지를 사용해야하는데 기존 에러코드를 수정할 수는 없어서 임시 사용
        if (isUserNotFound(loginEmail)) {
            throw new NotFoundUser("존재하지 않는 회원입니다.");
        }

        User findUser = findUserByEmail(loginEmail);
        String nickname = findUser.changeNickname(newNickname);

        return EditInitialNicknameResponse.of(nickname);
    }

    private boolean isUserNotFound(String loginEmail) {
        return !userRepository.findByEmail(loginEmail).isPresent();
    }

    @Transactional(readOnly = true)
    public String checkNicknameDuplication(String nickname) {
        if (isNicknameAlreadyExist(nickname)) {
            return "중복";
        }
        return "사용가능";
    }

    private boolean isNicknameAlreadyExist(String newNickname) {
        return userRepository.findByNickname(newNickname).isPresent();
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(NotFoundUser::new);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundUser::new);
    }
}
