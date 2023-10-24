package backend.VocaProject.vocabularyBookCategory;

import backend.VocaProject.domain.VocabularyBookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyBookCategoryRepository extends JpaRepository<VocabularyBookCategory, Long> {
    VocabularyBookCategory findByName(String categoryName);
}
