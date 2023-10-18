package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.ApprovalUpdateRequest;
import backend.VocaProject.admin.dto.UserListResponse;

import java.util.List;

public interface AdminService {

    List<UserListResponse> userList(Long adminId, String className, String approval);

    void userUpdateApproval(ApprovalUpdateRequest request);
}
