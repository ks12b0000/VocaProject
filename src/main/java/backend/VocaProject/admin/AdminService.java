package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.ApprovalUpdateRequest;
import backend.VocaProject.admin.dto.UserListResponse;
import backend.VocaProject.admin.dto.UserUpdateRequest;

import java.util.List;

public interface AdminService {

    List<UserListResponse> userList(Long adminId, String className, String approval);

    void userApprovalUpdate(ApprovalUpdateRequest request);

    void userUpdate(Long adminId, UserUpdateRequest request);

    void userDelete(String userLoginId);
}
