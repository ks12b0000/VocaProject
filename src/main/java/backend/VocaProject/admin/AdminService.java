package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.UserListResponse;

import java.util.List;

public interface AdminService {

    List<UserListResponse> userList(Long userId, String keyword, String approval);
}
