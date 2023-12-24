package backend.VocaProject.vocabularyTestSetting;

import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.domain.VocabularyTestSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyTestSettingRepository extends JpaRepository<VocabularyTestSetting, Long> {
    boolean existsByUserAndVocabularyBookCategoryAndFirstDayAndLastDay(@Param("user") User user, @Param("category") VocabularyBookCategory category,
                                                                       @Param("firstDay") int firstDay, @Param("lastDay") int lastDay);

    VocabularyTestSetting findByUserAndVocabularyBookCategoryAndFirstDayAndLastDay(@Param("user") User user, @Param("category") VocabularyBookCategory category,
                                                                                   @Param("firstDay") int firstDay, @Param("lastDay") int lastDay);

    @Query("select targetScore " +
            "from VocabularyTestSetting " +
            "where user = :user And vocabularyBookCategory = :category And firstDay = :firstDay And lastDay = :lastDay")
    Integer findByUserVocabularyTestTargetScore(@Param("user") User user, @Param("category") VocabularyBookCategory category,
                                                                                   @Param("firstDay") int firstDay, @Param("lastDay") int lastDay);
}
