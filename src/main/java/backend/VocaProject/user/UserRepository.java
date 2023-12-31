package backend.VocaProject.user;

import backend.VocaProject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByApproval(@Param("approval") String approval);
    Optional<User> findByLoginId(@Param("loginId") String loginId);
    List<User> findByClassNameAndApproval(@Param("className") String className, @Param("approval") String approval);
}
