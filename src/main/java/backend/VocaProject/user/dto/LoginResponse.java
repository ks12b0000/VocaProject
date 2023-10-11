package backend.VocaProject.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {

    private Long id;
    private String jwtToken;

    public LoginResponse(Long id, String jwtToken) {
        this.id = id;
        this.jwtToken = jwtToken;
    }
}
