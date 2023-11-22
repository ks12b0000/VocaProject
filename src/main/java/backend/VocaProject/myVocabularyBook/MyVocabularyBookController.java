package backend.VocaProject.myVocabularyBook;

import backend.VocaProject.response.BaseResponse;
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
@Tag(name = "MyVocabularyBook", description = "나만의 단어장 API")
@RequestMapping("/api/auth")
public class MyVocabularyBookController {

    private final MyVocabularyBookService myVocabularyBookService;

    @Operation(summary = "나만의 단어장에 단어 추가 API", responses = {
            @ApiResponse(responseCode = "200", description = "나만의 단어장에 단어를 추가했습니다.")
    })
    @Tag(name = "MyVocabularyBook")
    @PostMapping("/my-vocabulary-book/{wordId}")
    public ResponseEntity myVocabularyBookInsert(Authentication auth, @PathVariable Long wordId) {
        myVocabularyBookService.myVocabularyBookInsert(auth, wordId);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "나만의 단어장에 단어를 추가했습니다."));
    }

    @Operation(summary = "나만의 단어장에 단어 삭제 API", responses = {
            @ApiResponse(responseCode = "200", description = "나만의 단어장에 단어를 삭제했습니다.")
    })
    @Tag(name = "MyVocabularyBook")
    @DeleteMapping("/my-vocabulary-book/{wordId}")
    public ResponseEntity myVocabularyBookDelete(Authentication auth, @PathVariable Long wordId) {
        myVocabularyBookService.myVocabularyBookDelete(auth, wordId);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "나만의 단어장에 단어를 삭제했습니다."));
    }
}
