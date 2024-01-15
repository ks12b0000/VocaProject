package backend.VocaProject.vocabularyTest;

import backend.VocaProject.admin.dto.VocabularyTestResultListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VocabularyTestCustomRepository {

    List<VocabularyTestResultListResponse> findByTestResultList(@Param("size") int size, @Param("lastModifiedAt") LocalDateTime lastModifiedAt, @Param("className") String className);
}
