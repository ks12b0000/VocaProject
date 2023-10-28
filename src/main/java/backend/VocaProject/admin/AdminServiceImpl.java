package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.ApprovalUpdateRequest;
import backend.VocaProject.admin.dto.UserListResponse;
import backend.VocaProject.admin.dto.UserUpdatePwRequest;
import backend.VocaProject.admin.dto.UserUpdateRequest;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.response.BaseException;
import backend.VocaProject.user.UserRepository;
import backend.VocaProject.vocabularyBook.VocabularyBookRepository;
import backend.VocaProject.vocabularyBookCategory.VocabularyBookCategoryRepository;
import lombok.RequiredArgsConstructor;
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

    /**
     * 유저 목록 조회
     * @return
     */
    @Override
    public List<UserListResponse> userList(Long adminId, String className, String approval) {
        User user = userRepository.findById(adminId).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));

        // 유저의 클래스 이름이 master면(마스터 관리자)
        if (user.getClassName().equals("master")) {
            // 입력한 keyword에 맞는 클래스의 승인 여부가 Y인 유저 목록
            List<UserListResponse> listByClass = userRepository.findByClassNameAndApproval(className, "Y").stream().map(UserListResponse::new).collect(Collectors.toList());
            return listByClass;
        }
        // 중간 관리자가 요청했을 경우 자기가 맡은 클래스의 승인 여부가 Y인 유저 목록만 조회
        else {
            className = user.getClassName();
            List<UserListResponse> listByClass = userRepository.findByClassNameAndApproval(className, "Y").stream().map(UserListResponse::new).collect(Collectors.toList());
            return listByClass;
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
        User user = userRepository.findByLoginId(request.getUserLoginId()).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));

        user.updateApprovalAndRole(request.getApproval(), request.getRole());
    }

    /**
     * 유저 정보 변경
     * @param adminId
     * @param request
     */
    @Override
    @Transactional
    public void userUpdate(Long adminId, UserUpdateRequest request) {
        User admin = userRepository.findById(adminId).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));
        User user = userRepository.findByLoginId(request.getUserLoginId()).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));

        // 마스터 관리자일 경우 role, className 변경 가능
        if (admin.getClassName().equals("master")) {
            user.updateUser(request.getRole(), request.getClassName());
        }
        else {
            // 중간 관리자는 자기가 맡은 클래스의 유저만 변경 가능
            if (!admin.getClassName().equals(user.getClassName())) throw new BaseException(WITHOUT_ACCESS_USER);
            // 중간 관리자는 className만 변경 가능
            user.updateUser(request.getClassName());
        }
    }

    /**
     * 유저 비밀번호 변경
     * @param adminId
     * @param request
     */
    @Override
    @Transactional
    public void userPasswordUpdate(Long adminId, String userLoginId, UserUpdatePwRequest request) {
        User admin = userRepository.findById(adminId).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));
        User user = userRepository.findByLoginId(userLoginId).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));
        String enPassword = bCryptPasswordEncoder.encode(request.getPassword());

        if (admin.getClassName().equals("master")) {
            user.updateUserPassword(enPassword);
        }
        else {
            // 중간 관리자는 자기가 맡은 클래스의 유저만 변경 가능
            if (!admin.getClassName().equals(user.getClassName())) throw new BaseException(WITHOUT_ACCESS_USER);
            user.updateUserPassword(enPassword);
        }
    }

    /**
     * 유저 삭제
     * @param userLoginId
     */
    @Override
    @Transactional
    public void userDelete(String userLoginId) {
        User user = userRepository.findByLoginId(userLoginId).orElseThrow(() -> new BaseException(NON_EXISTENT_USER));

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
}
