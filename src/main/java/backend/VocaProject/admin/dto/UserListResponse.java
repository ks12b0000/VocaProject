package backend.VocaProject.admin.dto;

import backend.VocaProject.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserListResponse {

    private Long userId;

    private String username;

    private String loginId;

    private String role;

    private String approval;

    private String className;

    @Builder
    public UserListResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.loginId = user.getLoginId();
        this.role = user.getRole();
        this.approval = user.getApproval();
        this.className = user.getClassName();
    }
}
