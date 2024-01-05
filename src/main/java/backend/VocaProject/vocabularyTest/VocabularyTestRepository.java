package backend.VocaProject.vocabularyTest;

import backend.VocaProject.admin.dto.VocabularyTestResultListResponse;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.domain.VocabularyTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyTestRepository extends JpaRepository<VocabularyTest, Long> {

    @Query("select testCount " +
            "from VocabularyTest " +
            "where user = :user And vocabularyBookCategory = :category And firstDay = :firstDay And lastDay = :lastDay")
    Integer findByTestCount(@Param("user") User user, @Param("category") VocabularyBookCategory category,
                                                                                   @Param("firstDay") int firstDay, @Param("lastDay") int lastDay);

    VocabularyTest findByUserAndVocabularyBookCategoryAndFirstDayAndLastDay(@Param("user") User user, @Param("category") VocabularyBookCategory category,
                                                             @Param("firstDay") int firstDay, @Param("lastDay") int lastDay);

    VocabularyTest findTop1ByUserAndVocabularyBookCategoryOrderByModifiedAtDesc(@Param("user") User user, @Param("category") VocabularyBookCategory category);

    @Query("select wrongWords " +
            "from VocabularyTest " +
            "where user = :user")
    List<String> findByUserWrongWords(@Param("user") User user);

    @Query("select new backend.VocaProject.admin.dto.VocabularyTestResultListResponse(" +
            "a.user.username, a.testCount, a.result, a.record, a.vocabularyBookCategory.name, a.firstDay, a.lastDay, a.modifiedAt, " +
                "(select b.learningTime " +
                "from VocabularyLearning b " +
                "where b.user = a.user AND b.vocabularyBookCategory = a.vocabularyBookCategory AND b.firstDay = a.firstDay AND b.lastDay = a.lastDay" +
                ") as learningTime" +
            ") " +
            "from VocabularyTest a " +
            "where (:className IS NULL OR a.user.className = :className)")
    List<VocabularyTestResultListResponse> findByTestResultList(Pageable pageable, @Param("className") String className);
}
