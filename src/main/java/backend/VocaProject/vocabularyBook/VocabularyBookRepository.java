package backend.VocaProject.vocabularyBook;

import backend.VocaProject.domain.VocaBook;
import backend.VocaProject.domain.VocaBookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyBookRepository extends JpaRepository<VocaBook, Long> {
    List<VocaBook> findByVocaBookCategory(VocaBookCategory vocaBookCategory);
}
