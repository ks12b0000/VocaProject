package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.*;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.domain.VocabularyTestSetting;
import backend.VocaProject.response.BaseException;
import backend.VocaProject.user.UserRepository;
import backend.VocaProject.vocabularyBook.VocabularyBookRepository;
import backend.VocaProject.vocabularyBookCategory.VocabularyBookCategoryRepository;
import backend.VocaProject.vocabularyTest.VocabularyTestRepository;
import backend.VocaProject.vocabularyTestSetting.VocabularyTestSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    /**
     * 유저 목록 조회
     * @return
     */
    @Override
    public List<UserListResponse> userList(Authentication admin, String className) {
        User user = (User) admin.getPrincipal();

        if (user.getClassName().equals("master")) {
            return className.equals("all") ?
                    userRepository.findByApproval("Y").stream().map(UserListResponse::new).collect(Collectors.toList()) :
                    userRepository.findByClassNameAndApproval(className, "Y").stream().map(UserListResponse::new).collect(Collectors.toList());
        } else {
            className = user.getClassName();
            return userRepository.findByClassNameAndApproval(className, "Y").stream().map(UserListResponse::new).collect(Collectors.toList());
        }
    }

    /**
     * 유저 승인X 목록 조회
     * @return
     */
    @Override
    public List<UserListResponse> userNonApprovalList() {
        return userRepository.findByApproval("N").stream().map(UserListResponse::new).collect(Collectors.toList());
    }

    /**
     * 유저 승인 여부 변경
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
     * @param admin
     * @param request
     */
    @Override
    @Transactional
    public void userUpdate(Authentication admin, UserUpdateRequest request) {
        User adminUser = (User) admin.getPrincipal();
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));

        // 마스터 관리자일 경우 role, className 변경 가능
        if (adminUser.getClassName().equals("master")) {
            user.updateUser(request.getRole(), request.getClassName());
        }
        else {
            // 중간 관리자는 자기가 맡은 클래스의 유저만 변경 가능
            if (!adminUser.getClassName().equals(user.getClassName())) throw new BaseException(WITHOUT_ACCESS_USER);
            // 중간 관리자는 className만 변경 가능
            user.updateUser(request.getClassName());
        }
    }

    /**
     * 유저 비밀번호 변경
     * @param admin
     * @param request
     */
    @Override
    @Transactional
    public void userPasswordUpdate(Authentication admin, UserUpdatePwRequest request) {
        User adminUser = (User) admin.getPrincipal();
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));
        String enPassword = bCryptPasswordEncoder.encode(request.getPassword());

        if (adminUser.getClassName().equals("master")) {
            user.updateUserPassword(enPassword);
        }
        else {
            // 중간 관리자는 자기가 맡은 클래스의 유저만 변경 가능
            if (!adminUser.getClassName().equals(user.getClassName())) throw new BaseException(WITHOUT_ACCESS_USER);
            user.updateUserPassword(enPassword);
        }
    }

    /**
     * 유저 삭제
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

        if (vocabularyTestSetting == null) {
            if (isAdminUserMaster || (isAdminUserSameClass && !isAdminUserMaster)) {
                vocabularyTestSetting = new VocabularyTestSetting(category, user, request.getTargetScore(), request.getFirstDay(), request.getLastDay());
                vocabularyTestSettingRepository.save(vocabularyTestSetting);
            } else {
                throw new BaseException(WITHOUT_ACCESS_USER);
            }
        } else {
            if (isAdminUserMaster || (isAdminUserSameClass && !isAdminUserMaster)) {
                vocabularyTestSetting.updateTargetScore(request.getTargetScore());
            } else {
                throw new BaseException(WITHOUT_ACCESS_USER);
            }
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
    public List<VocabularyTestResultListResponse> vocabularyTestResultLists(Authentication admin, Pageable pageable) {
        User adminUser = (User) admin.getPrincipal();
        String className = adminUser.getClassName();

        List<VocabularyTestResultListResponse> responses = vocabularyTestRepository.findByTestResultList(pageable, className.equals("master") ? null : className);

        return responses;
    }
}
