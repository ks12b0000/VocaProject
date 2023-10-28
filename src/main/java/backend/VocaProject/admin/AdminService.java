package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.ApprovalUpdateRequest;
import backend.VocaProject.admin.dto.UserListResponse;
import backend.VocaProject.admin.dto.UserUpdatePwRequest;
import backend.VocaProject.admin.dto.UserUpdateRequest;

import java.util.List;

public interface AdminService {

    List<UserListResponse> userList(Long adminId, String className);

    List<UserListResponse> userNonApprovalList();

    void userApprovalUpdate(ApprovalUpdateRequest request);

    void userUpdate(Long adminId, UserUpdateRequest request);

    void userPasswordUpdate(Long adminId, String userLoginId, UserUpdatePwRequest request);

    void userDelete(String userLoginId);

    void vocabularyBookDelete(Long categoryId);
}
