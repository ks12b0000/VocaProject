package backend.VocaProject.vocabularyTest;

import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.domain.VocabularyTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyTestRepository extends JpaRepository<VocabularyTest, Long> {

    @Query("select testCount " +
            "from VocabularyTest " +
            "where user = :user And vocabularyBookCategory = :category And firstDay = :firstDay And lastDay = :lastDay")
    Integer findByUserAndVocabularyBookCategoryAndFirstDayAndLastDay(@Param("user") User user, @Param("category") VocabularyBookCategory category,
                                                                                   @Param("firstDay") int firstDay, @Param("lastDay") int lastDay);
}
