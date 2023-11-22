package backend.VocaProject.vocabularyBook;

import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBook;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.vocabularyBook.dto.VocabularyBookResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocabularyBookRepository extends JpaRepository<VocabularyBook, Long> {
    List<VocabularyBook> findByVocabularyBookCategory(VocabularyBookCategory vocabularyBookCategory);

    void deleteByVocabularyBookCategory(VocabularyBookCategory vocabularyBookCategory);

    @Query("select new backend.VocaProject.vocabularyBook.dto.VocabularyBookResponse(" +
            "a.id, a.word, a.meaning) " +
            "from VocabularyBook a " +
            "where a.vocabularyBookCategory = :category and a.day >= :firstDay and a.day <= :lastDay")
    List<VocabularyBookResponse> findByVocabularyList(@Param("category") VocabularyBookCategory category, @Param("firstDay") int firstDay, @Param("lastDay") int lastDay);
}
