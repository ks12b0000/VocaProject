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
public class ApprovalUpdateRequest {

    @Schema(description = "유저 로그인 아이디", example = "example000", required = true)
    @NotBlank(message = "아이디를 입력하세요.", groups = ValidationGroup.NotBlankGroup.class)
    private String userLoginId;

    @Schema(description = "승인 여부", example = "Y", required = true)
    @NotBlank(message = "승인 여부를 입력하세요.", groups = ValidationGroup.NotBlankGroup.class)
    private String approval;
}
