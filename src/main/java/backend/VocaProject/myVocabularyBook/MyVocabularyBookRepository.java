package backend.VocaProject.myVocabularyBook;

import backend.VocaProject.domain.MyVocabularyBook;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyVocabularyBookRepository extends JpaRepository<MyVocabularyBook, Long> {

    Optional<MyVocabularyBook> findByUserAndVocabularyBook(User user, VocabularyBook vocabularyBook);

    boolean existsByUserAndVocabularyBook(User user, VocabularyBook vocabularyBook);
}
