package backend.VocaProject.user.dto;

import backend.VocaProject.response.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static backend.VocaProject.response.ValidationGroup.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {

    @Schema(description = "이름", example = "홍길동", required = true)
    @NotBlank(message = "이름을 입력하세요.", groups = NotBlankGroup.class)
    private String username;

    @Schema(description = "로그인 아이디", example = "example12345", required = true)
    @NotBlank(message = "아이디를 입력하세요.", groups = NotBlankGroup.class)
    private String loginId;

    @Schema(description = "비밀번호", example = "example1234", required = true)
    @NotBlank(message = "비밀번호를 입력하세요.", groups = NotBlankGroup.class)
    private String password;
}
