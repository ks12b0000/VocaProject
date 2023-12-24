package backend.VocaProject.vocabularyTest;

import backend.VocaProject.response.BaseResponse;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestListResponse;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "VocabularyTest", description = "단어 테스트 API")
@RequestMapping("/api/auth")
public class VocabularyTestController {

    private final VocabularyTestService vocabularyTestService;

    @Operation(summary = "단어 테스트 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "단어 테스트 조회에 성공했습니다.")
    })
    @Tag(name = "VocabularyTest")
    @GetMapping("/vocabulary-test")
    public ResponseEntity vocabularyTest(Authentication auth, @RequestParam Long categoryId, @RequestParam int firstDay, @RequestParam int lastDay) {
        VocabularyTestListResponse response = vocabularyTestService.vocabularyTest(auth, categoryId, firstDay, lastDay);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "단어 테스트 조회에 성공했습니다.", response));
    }
}
