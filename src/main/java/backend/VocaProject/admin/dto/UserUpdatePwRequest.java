package backend.VocaProject.admin.dto;

import backend.VocaProject.response.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdatePwRequest {

    @Schema(description = "비밀번호", example = "example1234", required = true)
    @NotBlank(message = "비밀번호를 입력하세요.", groups = ValidationGroup.NotBlankGroup.class)
    private String password;
}
