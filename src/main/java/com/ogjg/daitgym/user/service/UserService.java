package com.ogjg.daitgym.user.service;

import com.ogjg.daitgym.approval.repository.*;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.WrongApproach;
import com.ogjg.daitgym.domain.*;
import com.ogjg.daitgym.domain.follow.Follow;
import com.ogjg.daitgym.follow.repository.FollowRepository;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalRepository;
import com.ogjg.daitgym.s3.repository.S3Repository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.ogjg.daitgym.domain.ApproveStatus.*;

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

    private final S3Repository s3Repository;

    @Value("${cloud.aws.default.profile-img}")
    private String AWS_DEFAULT_PROFILE_IMG;

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

        if (isNicknameAlreadyExist(request.getNickname())) {
            throw new AlreadyExistNickname();
        }

        String newImgUrl;
        String findImgUrl = user.getImageUrl();

        // default 이미지 url 사용 시 삭제 방지
        if (!AWS_DEFAULT_PROFILE_IMG.equals(findImgUrl)) {
            s3Repository.deleteImageFromS3(findImgUrl);
        }

        if (isEmptyFile(multipartFile)) {
            newImgUrl = findImgUrl;
        } else {
            // s3에 uuid로 랜덤 이름 생성해서 저장
            newImgUrl = s3Repository.uploadImageToS3(multipartFile);
        }

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

    private static boolean isEmptyFile(MultipartFile multipartFile) {
        return multipartFile == null || multipartFile.isEmpty();
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
    public void applyForApproval(String loginEmail, ApplyForApprovalRequest request, List<MultipartFile> awardImgs, List<MultipartFile> certificationImgs) {
        User user = findUserByEmail(loginEmail);

        validateOmittedCases(request, awardImgs, certificationImgs);

        // s3에 이미지들 저장
        List<String> awardImgUrls = saveInS3IfExistBoth(request.getAwards(), awardImgs);
        List<String> certificationImgUrls = saveInS3IfExistBoth(request.getCertifications(), certificationImgs);

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

        List<AwardImage> awardImages = awardImgUrls.stream()
                .map(AwardImage::of)
                .map((awardImage -> awardImage.addAward(savedAwards.get(0))))
                .toList();

        List<CertificationImage> certificationImages = certificationImgUrls.stream()
                .map(CertificationImage::of)
                .map((certificationImage -> certificationImage.addAward(savedCertifications.get(0))))
                .toList();

        awardImageRepository.saveAll(awardImages);
        certificationImageRepository.saveAll(certificationImages);
    }

    private void validateOmittedCases(ApplyForApprovalRequest request, List<MultipartFile> awardImgs, List<MultipartFile> certificationImgs) {
        validateAllOmitted(request);
        validateOnlyHasImagesCases(request, awardImgs, certificationImgs);
    }

    private void validateAllOmitted(ApplyForApprovalRequest request) {
        if (isEmptyCollection(request.getAwards()) && isEmptyCollection(request.getCertifications())) {
            throw new EmptyTrainerApplyException();
        }
    }

    private void validateOnlyHasImagesCases(ApplyForApprovalRequest request, List<MultipartFile> awardImgs, List<MultipartFile> certificationImgs) {
        if (isEmptyCollection(request.getAwards()) && !isFilesListNull(awardImgs)) {
            throw new EmptyTrainerApplyException();
        }

        if (isEmptyCollection(request.getCertifications()) && !isFilesListNull(certificationImgs)) {
            throw new EmptyTrainerApplyException();
        }
    }

    /**
     * 자격증과 이미지 혹은 수상과 이미지가 모두 값이 존재           -->  s3에 저장하고 url들을 반환
     * 자격증과 이미지 혹은 수상과 이미지가 모두 null 혹은 비어있다면 -->  빈 리스트 반환
     */
    private List<String> saveInS3IfExistBoth(Collection<?> submitted, List<MultipartFile> imgFiles) {
        if (!isEmptyCollection(submitted) && !isFilesListNull(imgFiles)) {
            return imgFiles.stream()
                    .map((imgFile) -> s3Repository.uploadImageToS3(imgFile))
                    .toList();
        }

        return Collections.emptyList();
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
        String newNickname = request.getNickname();

        if (isNicknameAlreadyExist(newNickname)) {
            throw new AlreadyExistNickname();
        }

        // 해당 메시지를 사용해야하는데 기존 에러코드를 수정할 수는 없어서 임시 사용
        if (isUserNotFoundByEmail(loginEmail)) {
            throw new NotFoundUser("존재하지 않는 회원입니다.");
        }

        User findUser = findUserByEmail(loginEmail);
        String nickname = findUser.changeNickname(newNickname);

        return EditInitialNicknameResponse.of(nickname);
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
