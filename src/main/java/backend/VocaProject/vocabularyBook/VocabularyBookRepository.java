package backend.VocaProject.vocabularyBook;

import backend.VocaProject.domain.VocabularyBook;
import backend.VocaProject.domain.VocabularyBookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyBookRepository extends JpaRepository<VocabularyBook, Long> {
    List<VocabularyBook> findByVocabularyBookCategory(VocabularyBookCategory vocabularyBookCategory);
}
