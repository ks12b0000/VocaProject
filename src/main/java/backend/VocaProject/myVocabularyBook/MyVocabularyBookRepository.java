package backend.VocaProject.myVocabularyBook;

import backend.VocaProject.domain.MyVocabularyBook;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBook;
import backend.VocaProject.myVocabularyBook.dto.MyVocabularyBookListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyVocabularyBookRepository extends JpaRepository<MyVocabularyBook, Long> {

    Optional<MyVocabularyBook> findByUserAndVocabularyBook(User user, VocabularyBook vocabularyBook);

    boolean existsByUserAndVocabularyBook(User user, VocabularyBook vocabularyBook);

    @Query("select new backend.VocaProject.myVocabularyBook.dto.MyVocabularyBookListResponse(" +
            "a.vocabularyBook.id, a.vocabularyBook.word, a.vocabularyBook.meaning, a.vocabularyBook.vocabularyBookCategory.name) " +
            "from MyVocabularyBook a " +
            "where a.user = :user")
    List<MyVocabularyBookListResponse> findByMyVocabularyBookList(User user);
}
