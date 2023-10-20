package backend.VocaProject.vocaBookCategory;

import backend.VocaProject.domain.VocaBookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocaBookCategoryRepository extends JpaRepository<VocaBookCategory, Long> {
    VocaBookCategory findByName(String categoryName);
}
