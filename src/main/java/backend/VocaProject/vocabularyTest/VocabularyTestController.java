package backend.VocaProject.vocabularyTest;

import backend.VocaProject.response.BaseResponse;
import backend.VocaProject.response.ValidationSequence;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestListResponse;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestResultRequest;
import backend.VocaProject.vocabularyTest.dto.WrongWordsListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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

    @Operation(summary = "단어 테스트 결과 저장 API", responses = {
            @ApiResponse(responseCode = "201", description = "단어 테스트 결과 저장에 성공했습니다.")
    })
    @Tag(name = "VocabularyTest")
    @PostMapping("/vocabulary-test")
    public ResponseEntity vocabularyTestResultSave(Authentication auth, @Validated(ValidationSequence.class) @RequestBody VocabularyTestResultRequest request) {
        vocabularyTestService.vocabularyTestResultSave(auth, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(201, "단어 테스트 결과 저장에 성공했습니다."));
    }

    @Operation(summary = "단어 테스트 틀린 단어 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "단어 테스트 틀린 단어 조회에 성공했습니다.")
    })
    @Tag(name = "VocabularyTest")
    @GetMapping("/vocabulary-test/wrong/words")
    public ResponseEntity vocabularyTestWrongWords(Authentication auth) {
        WrongWordsListResponse response = vocabularyTestService.vocabularyTestWrongWords(auth);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "단어 테스트 틀린 단어 조회에 성공했습니다.", response));
    }
}
