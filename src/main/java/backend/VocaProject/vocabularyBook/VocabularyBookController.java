package backend.VocaProject.vocabularyBook;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "VocabularyBook", description = "단어장 API")
public class VocabularyBookController {

    private final VocabularyBookService vocaBookService;

}
