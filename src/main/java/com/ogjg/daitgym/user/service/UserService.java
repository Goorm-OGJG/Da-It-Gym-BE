package com.ogjg.daitgym.user.service;

import com.ogjg.daitgym.approval.repository.*;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.WrongApproach;
import com.ogjg.daitgym.domain.*;
import com.ogjg.daitgym.follow.repository.FollowRepository;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalRepository;
import com.ogjg.daitgym.s3.repository.S3Repository;
import com.ogjg.daitgym.user.dto.request.ApplyForApprovalRequest;
import com.ogjg.daitgym.user.dto.request.EditNicknameRequest;
import com.ogjg.daitgym.user.dto.request.EditUserProfileRequest;
import com.ogjg.daitgym.user.dto.request.RegisterInbodyRequest;
import com.ogjg.daitgym.user.dto.response.EditInitialNicknameResponse;
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

    private final S3Repository s3Repository;

    @Value("${cloud.aws.default.profile-img}")
    private String AWS_DEFAULT_PROFILE_IMG;

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

        String newImgUrl;
        String findImgUrl = user.getImageUrl();

        // default 이미지 url 사용 시 삭제 방지
        if (!findImgUrl.equals(AWS_DEFAULT_PROFILE_IMG)) {
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
                request.getIntroduction(),
                healthClub,
                request.getPreferredSplit()
        );
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

    @Transactional
    public void applyForApproval(String loginEmail, ApplyForApprovalRequest request, List<MultipartFile> awardImgs, List<MultipartFile> certificationImgs) {
        User user = findUserByEmail(loginEmail);

       /* //  수상 경력은 있으나 이미지가 누락됨
        if (isEmptyCollection(request.getAwards()) && !isEmptyCollection(awardImgs)) {

        }
        // 자격증은 있으나 이미지가 누락됨
        if (isEmptyCollection(request.getCertifications()) && !isEmptyCollection(awardImgs)) {

        }*/

        if (isEmptySubmit(request.getAwards(), awardImgs) && isEmptySubmit(request.getCertifications(), certificationImgs)) {
            throw new EmptyTrainerApplyException();
        }

        // db 저장 로직
        Approval savedApproval = approvalRepository.save(Approval.builder().build());

        // 수상 경력 관련해서 입력된 경우만 수행
        if (!isEmptySubmit(request.getAwards(), awardImgs)) {
            // s3 저장 로직
            List<String> awardImgUrls = awardImgs.stream()
                    .map((imgFile) -> s3Repository.uploadImageToS3(imgFile))
                    .toList();

            // db 저장 로직
            List<Award> awards = awardRepository.saveAll(toAwards(user, request, awardImgUrls, savedApproval));
            awards.stream()
                    .forEach(award -> awardImageRepository.saveAll(award.getAwardImages()));
        }

        // 자격증 관련해서 입력된 경우만 수행
        if (!isEmptySubmit(request.getCertifications(), certificationImgs)) {
            // s3 저장 로직
            List<String> certificationImgUrls = certificationImgs.stream()
                    .map((imgFile) -> s3Repository.uploadImageToS3(imgFile))
                    .toList();

            // db 저장 로직
            List<Certification> certifications = certificationRepository.saveAll(toCertifications(user, request, certificationImgUrls, savedApproval));
            certifications.stream()
                    .forEach(certification -> certificationImageRepository.saveAll(certification.getCertificationImages()));
        }
    }

    private boolean isEmptySubmit(Collection<?> submitted, List<MultipartFile> awardImgs) {
        return isEmptyCollection(submitted) && isEmptyCollection(awardImgs);
    }

    private boolean isEmptyCollection(Collection<?> collection) {
        return collection == null || collection.isEmpty();
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
