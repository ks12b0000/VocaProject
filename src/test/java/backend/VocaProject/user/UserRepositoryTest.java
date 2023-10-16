package backend.VocaProject.user;

import backend.VocaProject.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static final User user = new User("홍길동", "example123", "example123");

    @Test
    @DisplayName("유저 회원가입")
    void join() {
        // given
        // when
        User saveUser = userRepository.save(user);

        // then
        assertAll(
                () -> assertThat(saveUser.getUsername()).isEqualTo(user.getUsername()),
                () -> assertThat(saveUser.getLoginId()).isEqualTo(user.getLoginId()),
                () -> assertThat(saveUser.getPassword()).isEqualTo(user.getPassword()),
                () -> assertThat(saveUser.getRole()).isEqualTo("ROLE_USER"), // 유저 저장 시 기본 Role = ROLE_USER
                () -> assertThat(saveUser.getApproval()).isEqualTo("N") // 유저 저장 시 기본 Approval(승인여부) = N
        );
    }
}
