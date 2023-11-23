package backend.VocaProject.vocabularyBook.dto;

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
public class VocabularyLearningRequest {

    @Schema(description = "카테고리 아이디", example = "1", required = true)
    @NotNull(message = "카테고리 아이디를 입력하세요.", groups = ValidationGroup.NotNullGroup.class)
    private Long categoryId;

    @Schema(description = "학습 시간", example = "60", required = true)
    @NotNull(message = "학습 시간을 입력하세요.", groups = ValidationGroup.NotNullGroup.class)
    private Long learningTime;

    @Schema(description = "단어장 조회 기간", example = "1", required = true)
    @NotNull(message = "단어장 조회 기간을 입력하세요.", groups = ValidationGroup.NotNullGroup.class)
    private int firstDay;

    @Schema(description = "단어장 조회 기간", example = "1", required = true)
    @NotNull(message = "단어장 조회 기간을 입력하세요.", groups = ValidationGroup.NotNullGroup.class)
    private int lastDay;

}
