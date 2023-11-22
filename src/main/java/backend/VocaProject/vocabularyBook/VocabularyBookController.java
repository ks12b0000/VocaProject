package backend.VocaProject.vocabularyBook;

import backend.VocaProject.response.BaseResponse;
import backend.VocaProject.vocabularyBook.dto.VocabularyBookListResponse;
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
}
