package backend.VocaProject.vocabularyBook;

import backend.VocaProject.vocabularyBook.dto.VocabularyBookListResponse;
import backend.VocaProject.vocabularyBook.dto.VocabularyLearningRequest;
import org.springframework.security.core.Authentication;

public interface VocabularyBookService {

    VocabularyBookListResponse vocabularyBooks(Long categoryId, int firstDay, int lastDay);

    void vocabularyBookEndByLearningSave(Authentication auth, VocabularyLearningRequest request);
}
