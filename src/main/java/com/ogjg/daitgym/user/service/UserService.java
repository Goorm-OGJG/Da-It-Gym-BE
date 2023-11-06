package com.ogjg.daitgym.user.service;

import com.ogjg.daitgym.approval.repository.*;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.WrongApproach;
import com.ogjg.daitgym.domain.*;
import com.ogjg.daitgym.follow.repository.FollowRepository;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalRepository;
import com.ogjg.daitgym.user.dto.request.ApplyForApprovalRequest;
import com.ogjg.daitgym.user.dto.request.EditUserProfileRequest;
import com.ogjg.daitgym.user.dto.response.GetUserProfileGetResponse;
import com.ogjg.daitgym.user.exception.NotFoundUser;
import com.ogjg.daitgym.user.repository.HealthClubRepository;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(NotFoundUser::new);
    }

    private User findUserByEmail(String nickname) {
        return userRepository.findByEmail(nickname)
                .orElseThrow(NotFoundUser::new);
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
}