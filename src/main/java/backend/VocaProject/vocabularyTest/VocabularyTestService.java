package backend.VocaProject.vocabularyTest;

import backend.VocaProject.vocabularyTest.dto.VocabularyTestListResponse;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestResultRequest;
import org.springframework.security.core.Authentication;


public interface VocabularyTestService {

    VocabularyTestListResponse vocabularyTest(Authentication auth, Long categoryId, int firstDay, int lastDay);

    void vocabularyTestResultSave(Authentication auth, VocabularyTestResultRequest request);
}
