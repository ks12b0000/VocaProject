package backend.VocaProject.user;

import backend.VocaProject.domain.User;
import backend.VocaProject.user.dto.JoinRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @DisplayName("유저 회원가입")
    void join() {
        /**
         * 회원가입 성공
         */
        // given
        JoinRequest request = new JoinRequest("홍길동", "example123", "example123");
        User user = new User(request.getUsername(), request.getLoginId(), request.getPassword());
        // stub
        when(userRepository.save(any())).thenReturn(user);
        // then
        userService.userJoin(request);

        /**
         * 회원가입 실패 (로그인 아이디 중복)
         */
        // given
        JoinRequest request2 = new JoinRequest("홍길동", "example123", "example1");
        // stub
        when(userRepository.findByLoginId(user.getLoginId())).thenReturn(Optional.of(user));
        // then
        assertThatThrownBy(() -> userService.userJoin(request2)).hasMessage("중복된 아이디가 있습니다.");
    }
}
