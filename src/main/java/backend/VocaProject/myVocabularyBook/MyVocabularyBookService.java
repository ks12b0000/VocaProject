package backend.VocaProject.myVocabularyBook;

import org.springframework.security.core.Authentication;

public interface MyVocabularyBookService {

    void myVocabularyBookInsert(Authentication auth, Long wordId);

    void myVocabularyBookDelete(Authentication auth, Long wordId);
}
