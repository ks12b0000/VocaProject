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
public class UserUpdateRequest {

    @Schema(description = "유저 아이디", example = "1", required = true)
    @NotNull(message = "아이디를 입력하세요.", groups = ValidationGroup.NotNullGroup.class)
    private Long userId;

    @Schema(description = "유저 권한", example = "ROLE_USER", required = true)
    @NotBlank(message = "유저 권한을 입력하세요.", groups = ValidationGroup.NotBlankGroup.class)
    private String role;

    @Schema(description = "유저 클래스 이름", example = "중등 기초", required = true)
    private String className;

}
