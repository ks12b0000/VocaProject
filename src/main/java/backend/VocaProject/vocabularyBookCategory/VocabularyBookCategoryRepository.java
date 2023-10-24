package backend.VocaProject.vocabularyBookCategory;

import backend.VocaProject.domain.VocaBookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyBookCategoryRepository extends JpaRepository<VocaBookCategory, Long> {
    VocaBookCategory findByName(String categoryName);
}
