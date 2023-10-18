package backend.VocaProject.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {

    private Long id;
    private String jwtToken;

    private String role;

    public LoginResponse(Long id, String jwtToken, String role) {
        this.id = id;
        this.jwtToken = jwtToken;
        this.role = role;
    }
}
