package backend.VocaProject.admin.dto;

import backend.VocaProject.response.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdatePwRequest {

    @Schema(description = "유저 아이디", example = "1", required = true)
    @NotNull(message = "아이디를 입력하세요.", groups = ValidationGroup.NotNullGroup.class)
    private Long userId;

    @Schema(description = "비밀번호", example = "example1234", required = true)
    @NotBlank(message = "비밀번호를 입력하세요.", groups = ValidationGroup.NotBlankGroup.class)
    private String password;
}
