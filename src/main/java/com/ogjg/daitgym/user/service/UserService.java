package com.ogjg.daitgym.user.service;

import com.ogjg.daitgym.approval.repository.*;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.WrongApproach;
import com.ogjg.daitgym.domain.*;
import com.ogjg.daitgym.domain.follow.Follow;
import com.ogjg.daitgym.follow.repository.FollowRepository;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalRepository;
import com.ogjg.daitgym.s3.service.S3UserService;
import com.ogjg.daitgym.user.dto.request.ApplyForApprovalRequest;
import com.ogjg.daitgym.user.dto.request.EditNicknameRequest;
import com.ogjg.daitgym.user.dto.request.EditUserProfileRequest;
import com.ogjg.daitgym.user.dto.request.RegisterInbodyRequest;
import com.ogjg.daitgym.user.dto.response.EditInitialNicknameResponse;
import com.ogjg.daitgym.user.dto.response.EditUserProfileResponse;
import com.ogjg.daitgym.user.dto.response.GetInbodiesResponse;
import com.ogjg.daitgym.user.dto.response.GetUserProfileGetResponse;
import com.ogjg.daitgym.user.exception.AlreadyExistNickname;
import com.ogjg.daitgym.user.exception.EmptyTrainerApplyException;
import com.ogjg.daitgym.user.exception.NotFoundUser;
import com.ogjg.daitgym.user.repository.HealthClubRepository;
import com.ogjg.daitgym.user.repository.InbodyRepository;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

import static com.ogjg.daitgym.domain.ApproveStatus.WAITING;

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

    private final S3UserService s3UserService;

    @Transactional(readOnly = true)
    public GetUserProfileGetResponse getUserProfile(String loginEmail, String nickname) {
        User targetUser = findUserByNickname(nickname);

        return GetUserProfileGetResponse.builder()
                .nickname(targetUser.getNickname())
                .preferredSplit(targetUser.getPreferredSplit().getTitle())
                .userProfileImgUrl(targetUser.getImageUrl())
                .introduction(targetUser.getIntroduction())
                .healthClubName(targetUser.getHealthClub().getName())
                .isFollower(followRepository.findByFollowPK(Follow.createFollowPK(targetUser.getEmail(), loginEmail)).isPresent())
                .role(targetUser.getRole())
                .journalCount(exerciseJournalRepository.countByUserEmail(targetUser.getEmail()))
                .followerCount(followRepository.countByFollowPKTargetEmail(targetUser.getEmail()))
                .followingCount(followRepository.countByFollowPKFollowerEmail(targetUser.getEmail()))
                .build();
    }

    @Transactional
    public EditUserProfileResponse editUserProfile(String loginEmail, String nickname, EditUserProfileRequest request, MultipartFile multipartFile) {
        User user = findUserByNickname(nickname);

        if (!loginEmail.equals(user.getEmail())) {
            throw new WrongApproach("본인의 프로필만 수정할 수 있습니다.");
        }

        if (isNicknameAlreadyExist(user.getNickname(), request.getNickname())) {
            throw new AlreadyExistNickname();
        }

        String originImgUrl = user.getImageUrl();
        String newImgUrl = s3UserService.saveProfileImage(multipartFile, originImgUrl);

        //todo 헬스장 조회 수정, 수정 시 헬스장 식별자를 추가로 받아와야한다.
        HealthClub healthClub = findOrUpdateHealthClub(request.getGymName());

        user.editProfile(
                newImgUrl,
                request.getNickname(),
                request.getIntroduction(),
                healthClub,
                request.getPreferredSplit()
        );

        userRepository.save(user);

        return EditUserProfileResponse.of(user);
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

    /**
     * 트레이너 심사 요청 기능
     */
    @Transactional
    public void applyForApproval(String loginEmail, ApplyForApprovalRequest request, List<MultipartFile> awardImageFiles, List<MultipartFile> certificationImageFiles) {
        User user = findUserByEmail(loginEmail);

        validateOmission(request, awardImageFiles, certificationImageFiles);

        // s3에 이미지들 저장
        List<String> awardImageUrls = s3UserService.saveCareerImages(request.getAwards(), awardImageFiles);
        List<String> certificationImageUrls = s3UserService.saveCareerImages(request.getCertifications(), certificationImageFiles);

        // db에 저장
        Approval savedApproval = approvalRepository.save(Approval.builder().approveStatus(WAITING).build());

        List<Award> awards = request.getAwards().stream()
                .map((dto) -> dto.toAward(user, savedApproval))
                .toList();
        List<Certification> certifications = request.getCertifications().stream()
                .map((dto) -> dto.toCertification(user, savedApproval))
                .toList();

        List<Award> savedAwards = awardRepository.saveAll(awards);
        List<Certification> savedCertifications = certificationRepository.saveAll(certifications);

        List<AwardImage> awardImages = savedAwards.get(0).saveImages(awardImageUrls);
        List<CertificationImage> certificationImages = savedCertifications.get(0).saveImages(certificationImageUrls);

        awardImageRepository.saveAll(awardImages);
        certificationImageRepository.saveAll(certificationImages);
    }

    private void validateOmission(ApplyForApprovalRequest request, List<MultipartFile> awardImages, List<MultipartFile> certificationImages) {
        validateAllOmitted(request);
        validateOnlyHasImages(request, awardImages, certificationImages);
    }

    private void validateAllOmitted(ApplyForApprovalRequest request) {
        if (isEmptyCollection(request.getAwards()) && isEmptyCollection(request.getCertifications())) {
            throw new EmptyTrainerApplyException();
        }
    }

    private void validateOnlyHasImages(ApplyForApprovalRequest request, List<MultipartFile> awardImages, List<MultipartFile> certificationImages) {
        if (isEmptyCollection(request.getAwards()) && !isFilesListNull(awardImages)) {
            throw new EmptyTrainerApplyException();
        }

        if (isEmptyCollection(request.getCertifications()) && !isFilesListNull(certificationImages)) {
            throw new EmptyTrainerApplyException();
        }
    }
    
    private boolean isEmptyCollection(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    // List<MultipartFile>은 빈 파일보내면 길이 1의 리스트를 반환
    private boolean isFilesListNull(List<MultipartFile> imgFiles) {
        return imgFiles == null || imgFiles.get(0).isEmpty();
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
        User findUser = findUserByEmail(loginEmail);
        String newNickname = request.getNickname();

        if (isNicknameAlreadyExist(findUser.getNickname(), newNickname)) {
            throw new AlreadyExistNickname();
        }

        // 해당 메시지를 사용해야하는데 기존 에러코드를 수정할 수는 없어서 임시 사용
        if (isUserNotFoundByEmail(loginEmail)) {
            throw new NotFoundUser("존재하지 않는 회원입니다.");
        }

        String changedNickname = findUser.changeNickname(newNickname);
        return EditInitialNicknameResponse.of(changedNickname);
    }

    @Transactional(readOnly = true)
    public String checkNicknameDuplication(String nickname, String newNickname) {
        if (isNicknameAlreadyExist(nickname, newNickname)) {
            return "중복";
        }
        return "사용가능";
    }

    private boolean isNicknameAlreadyExist(String nickname, String newNickname) {
        return !nickname.equals(newNickname) && userRepository.findByNickname(newNickname).isPresent();
    }

    @Transactional(readOnly = true)
    public void updateUserDeleted(String loginEmail) {
        if (isUserNotFoundByEmail(loginEmail)) {
            throw new NotFoundUser("존재하지 않는 회원입니다.");
        }

        User findUser = findUserByEmail(loginEmail);
        findUser.withdraw();
    }

    private boolean isUserNotFoundByEmail(String loginEmail) {
        return !userRepository.findByEmail(loginEmail).isPresent();
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
