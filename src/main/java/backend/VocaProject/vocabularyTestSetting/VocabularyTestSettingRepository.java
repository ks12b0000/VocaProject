package backend.VocaProject.vocabularyTestSetting;

import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.domain.VocabularyTestSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyTestSettingRepository extends JpaRepository<VocabularyTestSetting, Long> {
    boolean existsByUserAndVocabularyBookCategory(@Param("user") User user, @Param("category") VocabularyBookCategory category);
}
