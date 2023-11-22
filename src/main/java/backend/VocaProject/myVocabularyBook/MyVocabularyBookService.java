package backend.VocaProject.myVocabularyBook;

import backend.VocaProject.myVocabularyBook.dto.MyVocabularyBookListResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MyVocabularyBookService {

    void myVocabularyBookInsert(Authentication auth, Long wordId);

    void myVocabularyBookDelete(Authentication auth, Long wordId);

    List<MyVocabularyBookListResponse> myVocabularyBookList(Authentication auth);
}
