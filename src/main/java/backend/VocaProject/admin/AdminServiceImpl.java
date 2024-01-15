package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.*;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.domain.VocabularyTestSetting;
import backend.VocaProject.response.BaseException;
import backend.VocaProject.user.UserRepository;
import backend.VocaProject.vocabularyBook.VocabularyBookRepository;
import backend.VocaProject.vocabularyBookCategory.VocabularyBookCategoryRepository;
import backend.VocaProject.vocabularyTest.VocabularyTestCustomRepository;
import backend.VocaProject.vocabularyTest.VocabularyTestRepository;
import backend.VocaProject.vocabularyTestSetting.VocabularyTestSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static backend.VocaProject.response.BaseExceptionStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final VocabularyBookRepository vocabularyBookRepository;

    private final VocabularyBookCategoryRepository vocabularyBookCategoryRepository;

    private final VocabularyTestSettingRepository vocabularyTestSettingRepository;

    private final VocabularyTestRepository vocabularyTestRepository;

    private final VocabularyTestCustomRepository vocabularyTestCustomRepository;

    /**
     * 유저 목록 조회
     * masterAdmin이면 입력 받은 className으로(all = 전체) 어플 사용 승인된 유저를 조회하고,
     * middleAdmin이면 자신이 맡은 class의 어플 사용 승인된 유저들만 조회한다.
     * @return
     */
    @Override
    public List<UserListResponse> userList(Authentication admin, String className) {
        User adminUser = (User) admin.getPrincipal();

        String targetClassName = adminUser.getClassName().equals("master") ? className : adminUser.getClassName();

        return (targetClassName.equals("all")) ?
                userRepository.findByApproval("Y").stream().map(UserListResponse::new).collect(Collectors.toList()) :
                userRepository.findByClassNameAndApproval(targetClassName, "Y").stream().map(UserListResponse::new).collect(Collectors.toList());
    }

    /**
     * 유저 어플 사용 승인X 목록 조회
     * 어플 사용 승인이 되지 않은 모든 유저를 조회한다.
     * @return
     */
    @Override
    public List<UserListResponse> userNonApprovalList() {
        return userRepository.findByApproval("N").stream().map(UserListResponse::new).collect(Collectors.toList());
    }

    /**
     * 유저 어플 사용 승인 여부 변경
     * 어플 사용을 승인한다면 approval = Y, role = ROLE_USER(일반 유저) OR ROLE_MIDDLEA_ADMIN(중간 관리자)로 변경하고,
     * 어플 사용을 취소한다면 approval = N, role = ROLE_NULL로 변경한다.
     * @param request
     */
    @Override
    @Transactional
    public void userApprovalUpdate(ApprovalUpdateRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));

        user.updateApprovalAndRole(request.getApproval(), request.getRole());
    }

    /**
     * 유저 정보 변경
     * masterAdmind이면 전체 유저의 role, className을 변경이 가능하고,
     * middleAdmind이면 자신이 맡은 class의 유저들의 className만 변경이 가능하다.
     * @param admin
     * @param request
     */
    @Override
    @Transactional
    public void userUpdate(Authentication admin, UserUpdateRequest request) {
        User adminUser = (User) admin.getPrincipal();
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));

        if (adminUser.getClassName().equals("master")) {
            user.updateUser(request.getRole(), request.getClassName());
        } else {
            // middleAdmind이 자신이 맡은 클래스의 유저가 아닌 유저의 정보를 변경하려고 한다면 예외처리.
            if (!adminUser.getClassName().equals(user.getClassName())) {
                throw new BaseException(WITHOUT_ACCESS_USER);
            }
            user.updateUser(request.getClassName());
        }
    }

    /**
     * 유저 비밀번호 변경
     * masterAdmin이면 모든 유저의 비밀번호를 변경 가능하고,
     * middleAdmin이면 자신이 맡은 class의 유저들의 비밀번호만 변경이 가능하다.
     * @param admin
     * @param request
     */
    @Override
    @Transactional
    public void userPasswordUpdate(Authentication admin, UserUpdatePwRequest request) {
        User adminUser = (User) admin.getPrincipal();
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));
        String enPassword = bCryptPasswordEncoder.encode(request.getPassword());

        // middleAdmind이 자신이 맡은 클래스의 유저가 아닌 유저의 비밀번호를 변경하려고 한다면 예외처리.
        if (!adminUser.getClassName().equals("master") && !adminUser.getClassName().equals(user.getClassName())) {
            throw new BaseException(WITHOUT_ACCESS_USER);
        }

        user.updateUserPassword(enPassword);
    }

    /**
     * 유저 삭제
     * masterAdmin만 유저 삭제가 가능하다.
     * @param userId
     */
    @Override
    @Transactional
    public void userDelete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));

        userRepository.delete(user);
    }

    /**
     * 단어장 삭제
     * masterAdmin만 단어장 삭제가 가능하다.
     * @param categoryId
     */
    @Override
    @Transactional
    public void vocabularyBookDelete(Long categoryId) {
        VocabularyBookCategory vocabularyBookCategory = vocabularyBookCategoryRepository.findById(categoryId).orElseThrow(() -> new BaseException(NON_EXISTENT_VOCABULARY_BOOK));

        vocabularyBookRepository.deleteByVocabularyBookCategory(vocabularyBookCategory);
    }

    /**
     * 유저별 단어 테스트 목표 정답률 설정
     * 이미 단어 테스트 목표 정답률을 설정하였다면 targetScore를 수정하고
     * 설정하지 않았다면 category, user, teargetScore, firstDay, lastDay로 저장한다.
     * masterAdmin은 모든 유저의 목표 정답률 설정 및 수정이 가능하고,
     * middleAdmin은 자신이 맡은 class의 유저들의 목표 정답률 설정 및 수정이 가능하다.
     * @param admin
     * @param request
     */
    @Override
    @Transactional
    public void vocabularyTestSetting(Authentication admin, VocabularyTestSettingRequest request) {
        User adminUser = (User) admin.getPrincipal();
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));
        VocabularyBookCategory category = vocabularyBookCategoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new BaseException(NON_EXISTENT_VOCABULARY_BOOK));
        VocabularyTestSetting vocabularyTestSetting = vocabularyTestSettingRepository.findByUserAndVocabularyBookCategoryAndFirstDayAndLastDay(user, category, request.getFirstDay(), request.getLastDay());

        boolean isAdminUserMaster = adminUser.getClassName().equals("master");
        boolean isAdminUserSameClass = adminUser.getClassName().equals(user.getClassName());

        if ((isAdminUserMaster || isAdminUserSameClass) && vocabularyTestSetting == null) {
            vocabularyTestSetting = new VocabularyTestSetting(category, user, request.getTargetScore(), request.getFirstDay(), request.getLastDay());
            vocabularyTestSettingRepository.save(vocabularyTestSetting);
        } else if ((isAdminUserMaster || isAdminUserSameClass) && vocabularyTestSetting != null) {
            vocabularyTestSetting.updateTargetScore(request.getTargetScore());
        } else {
            // middleAdmind이 자신이 맡은 클래스의 유저가 아닌 유저의 목표 정답률을 설정하려고 한다면 예외처리.
            throw new BaseException(WITHOUT_ACCESS_USER);
        }
    }

    /**
     * 단어 테스트 결과 목록 조회
     * masterAdmin이면 전체 학생의 테스트 결과를 조회하고
     * middleAdmin이면 자신의 클래스의 학생들의 테스트 결과를 조회한다.
     * @param admin
     * @param pageable
     * @return
     */
    @Override
    public List<VocabularyTestResultListResponse> vocabularyTestResultLists(Authentication admin, int size, LocalDateTime lastModifiedAt) {
        User adminUser = (User) admin.getPrincipal();
        String className = adminUser.getClassName();

        List<VocabularyTestResultListResponse> responses = vocabularyTestCustomRepository.findByTestResultList(size, lastModifiedAt, className.equals("master") ? null : className);

        return responses;
    }
}
