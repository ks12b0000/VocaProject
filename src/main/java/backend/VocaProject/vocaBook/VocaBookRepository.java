package backend.VocaProject.vocaBook;

import backend.VocaProject.domain.VocaBook;
import backend.VocaProject.domain.VocaBookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocaBookRepository extends JpaRepository<VocaBook, Long> {
    List<VocaBook> findByVocaBookCategory(VocaBookCategory vocaBookCategory);
}
