package backend.VocaProject.vocabularyBook;

import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBook;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.vocabularyBook.dto.VocabularyBookResponse;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface VocabularyBookRepository extends JpaRepository<VocabularyBook, Long> {
    List<VocabularyBook> findByVocabularyBookCategory(VocabularyBookCategory vocabularyBookCategory);

    void deleteByVocabularyBookCategory(VocabularyBookCategory vocabularyBookCategory);

    @Query("select new backend.VocaProject.vocabularyBook.dto.VocabularyBookResponse(" +
            "a.id, a.word, a.meaning, (select b.user.id from MyVocabularyBook b where b.vocabularyBook.id = a.id) as myVocabularyBook) " +
            "from VocabularyBook a " +
            "where a.vocabularyBookCategory = :category and a.day >= :firstDay and a.day <= :lastDay")
    List<VocabularyBookResponse> findByVocabularyList(@Param("category") VocabularyBookCategory category, @Param("firstDay") int firstDay, @Param("lastDay") int lastDay);

    @Query(value = "WITH RankedMeanings AS ( " +
            "   SELECT w.word_id, w.word, w.meaning AS original_meaning, m.meaning, " +
            "       ROW_NUMBER() OVER (PARTITION BY w.word_id ORDER BY RAND()) AS rnk " +
            "   FROM vocabulary_book w " +
            "   JOIN ( " +
            "       SELECT word_id, meaning " +
            "       FROM ( " +
            "           SELECT word_id, meaning, ROW_NUMBER() OVER (PARTITION BY meaning ORDER BY RAND()) AS rnk " +
            "           FROM vocabulary_book " +
            "           GROUP BY word_id, meaning " +
            "       ) tmp " +
            "       WHERE rnk = 1 " +
            "   ) m ON m.meaning <> w.meaning " +
            "   WHERE w.category_id = :category and w.day >= :firstDay AND w.day <= :lastDay" +
            ") " +
            "SELECT " +
            "word_id, word, original_meaning, " +
            "MAX(CASE WHEN rnk = 1 THEN meaning END) AS wrongMeaning1, " +
            "MAX(CASE WHEN rnk = 2 THEN meaning END) AS wrongMeaning2, " +
            "MAX(CASE WHEN rnk = 3 THEN meaning END) AS wrongMeaning3, " +
            "MAX(CASE WHEN rnk = 4 THEN meaning END) AS wrongMeaning4 " +
            "FROM RankedMeanings " +
            "GROUP BY word_id, word, original_meaning " +
            "ORDER BY RAND()", nativeQuery = true)
    List<Tuple> findByVocabularyTestList(@Param("category") long category, @Param("firstDay") int firstDay, @Param("lastDay") int lastDay);

}
