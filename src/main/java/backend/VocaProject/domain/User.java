package backend.VocaProject.domain;

import backend.VocaProject.admin.dto.UserUpdateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;

    @Column(length = 10, nullable = false)
    private String username;

    @Column(length = 50, nullable = false, unique = true)
    private String loginId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String role;

    @Column(length = 5, nullable = false)
    private String approval;

    @Column(length = 100)
    private String className;

    public User(String username, String loginId, String password) {
        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.role = "ROLE_NULL";
        this.approval = "N";
    }

    // 승인 여부 변경시 role도 같이 변경. (approval = N이면 role = ROLE_NULL, approval = Y이면 role = ROLE_USER)
    public void updateApprovalAndRole(String approval, String role) {
        this.approval = approval;
        this.role = role;
    }

    // 마스터 관리자가 user Update(password, role, className 변경 가능)
    public void updateUser(String role, String className) {
        this.role = role;
        this.className = className;
    }

    // 중간 관리자가 user Update(className 변경 가능)
    public void updateUser(String className) {
        this.className = className;
    }

    public void updateUserPassword(String password) {
        this.password = password;
    }
}
