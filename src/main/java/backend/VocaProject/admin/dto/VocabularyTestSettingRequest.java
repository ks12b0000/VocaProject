package backend.VocaProject.admin.dto;

import backend.VocaProject.response.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyTestSettingRequest {

    @Schema(description = "유저 아이디", example = "1", required = true)
    @NotNull(message = "아이디를 입력하세요.", groups = ValidationGroup.NotNullGroup.class)
    private Long userId;

    @Schema(description = "카테고리 아이디", example = "1", required = true)
    @NotNull(message = "카테고리 입력하세요.", groups = ValidationGroup.NotNullGroup.class)
    private Long categoryId;

    @Schema(description = "목표 정답률", example = "80", required = true)
    @NotNull(message = "목표 정답률 입력하세요.", groups = ValidationGroup.NotNullGroup.class)
    private int targetScore;
}
