package backend.VocaProject.vocabularyBook;

import backend.VocaProject.response.BaseResponse;
import backend.VocaProject.vocabularyBook.dto.VocabularyBookListResponse;
import backend.VocaProject.vocabularyBook.dto.VocabularyLearningRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "VocabularyBook", description = "단어장 API")
@RequestMapping("/api/auth")
public class VocabularyBookController {

    private final VocabularyBookService vocabularyBookService;

    @Operation(summary = "단어장 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "단어장 조회에 성공했습니다.")
    })
    @Tag(name = "VocabularyBook")
    @GetMapping("/vocabulary-book")
    public ResponseEntity vocabularyBooks(@RequestParam Long categoryId, @RequestParam int firstDay, @RequestParam int lastDay) {
        VocabularyBookListResponse response = vocabularyBookService.vocabularyBooks(categoryId, firstDay, lastDay);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "단어장 조회에 성공했습니다.", response));
    }

    @Operation(summary = "단어장 학습 종료시 학습 저장 API", responses = {
            @ApiResponse(responseCode = "200", description = "단어장 학습 저장에 성공했습니다.")
    })
    @Tag(name = "VocabularyBook")
    @PostMapping("/vocabulary-book/learning")
    public ResponseEntity vocabularyBookEndByLearningSave(Authentication auth, @RequestBody VocabularyLearningRequest request) {
        vocabularyBookService.vocabularyBookEndByLearningSave(auth, request);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "단어장 학습 저장에 성공했습니다."));
    }
}
