package backend.VocaProject.vocabularyLearning;

import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.domain.VocabularyLearning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyLearningRepository extends JpaRepository<VocabularyLearning, Long> {

    VocabularyLearning findByUserAndVocabularyBookCategory(@Param("user") User user, @Param("category")VocabularyBookCategory category);
}
