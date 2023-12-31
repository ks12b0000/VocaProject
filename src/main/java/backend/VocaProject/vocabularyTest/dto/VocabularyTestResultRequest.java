package backend.VocaProject.vocabularyTest.dto;

import backend.VocaProject.response.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyTestResultRequest {

    @Schema(description = "카테고리 아이디", example = "1", required = true)
    @NotNull(message = "카테고리 아이디를 입력하세요.", groups = ValidationGroup.NotNullGroup.class)
    private Long categoryId;

    @Schema(description = "단어장 테스트 범위", example = "1", required = true)
    @NotNull(message = "단어장 테스트 범위를 입력하세요.", groups = ValidationGroup.NotNullGroup.class)
    private int firstDay;

    @Schema(description = "단어장 테스트 범위", example = "1", required = true)
    @NotNull(message = "단어장 테스트 범위를 입력하세요.", groups = ValidationGroup.NotNullGroup.class)
    private int lastDay;

    @Schema(description = "테스트 결과", example = "Fail (목표 80%)", required = true)
    @NotBlank(message = "테스트 결과를 입력하세요.", groups = ValidationGroup.NotBlankGroup.class)
    private String result;

    @Schema(description = "테스트 성적", example = "42/60", required = true)
    @NotBlank(message = "테스트 성적을 입력하세요.", groups = ValidationGroup.NotBlankGroup.class)
    private String record;

    @Schema(description = "틀린 단어")
    private List<String> wrongWord;
}
