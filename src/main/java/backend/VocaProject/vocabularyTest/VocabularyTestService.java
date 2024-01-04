package backend.VocaProject.vocabularyTest;

import backend.VocaProject.vocabularyTest.dto.VocabularyTestListResponse;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestResultRequest;
import backend.VocaProject.vocabularyTest.dto.WrongWordsListResponse;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface VocabularyTestService {

    VocabularyTestListResponse vocabularyTest(Authentication auth, Long categoryId, int firstDay, int lastDay);

    void vocabularyTestResultSave(Authentication auth, VocabularyTestResultRequest request);

    WrongWordsListResponse vocabularyTestWrongWords(Authentication auth);
}
