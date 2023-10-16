package backend.VocaProject.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
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

    @Column(length = 10, nullable = false)
    private String role;

    @Column(length = 5, nullable = false)
    private String approval;

    public User(String username, String loginId, String password) {
        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.role = "ROLE_USER";
        this.approval = "N";
    }


}
