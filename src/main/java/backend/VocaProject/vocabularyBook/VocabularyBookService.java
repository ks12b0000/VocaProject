package backend.VocaProject.vocabularyBook;

import backend.VocaProject.vocabularyBook.dto.VocabularyBookListResponse;

public interface VocabularyBookService {

    VocabularyBookListResponse vocabularyBooks(Long categoryId, int firstDay, int lastDay);
}
