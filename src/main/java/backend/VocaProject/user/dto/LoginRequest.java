package backend.VocaProject.user.dto;

import backend.VocaProject.response.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Schema(description = "로그인 아이디", example = "example12345", required = true)
    @NotBlank(message = "아이디를 입력하세요.", groups = ValidationGroup.NotBlankGroup.class)
    private String loginId;

    @Schema(description = "비밀번호", example = "example1234", required = true)
    @NotBlank(message = "비밀번호를 입력하세요.", groups = ValidationGroup.NotBlankGroup.class)
    private String password;
}
