package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AdminService {

    List<UserListResponse> userList(Authentication admin, String className);

    List<UserListResponse> userNonApprovalList();

    void userApprovalUpdate(ApprovalUpdateRequest request);

    void userUpdate(Authentication admin, UserUpdateRequest request);

    void userPasswordUpdate(Authentication admin, UserUpdatePwRequest request);

    void userDelete(Long userId);

    void vocabularyBookDelete(Long categoryId);

    void vocabularyTestSetting(Authentication admin, VocabularyTestSettingRequest request);

    List<VocabularyTestResultListResponse> vocabularyTestResultLists(Authentication admin, Pageable pageable);
}
