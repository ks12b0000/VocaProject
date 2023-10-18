package backend.VocaProject.user;

import backend.VocaProject.domain.User;
import backend.VocaProject.response.BaseException;
import backend.VocaProject.user.dto.JoinRequest;
import backend.VocaProject.user.dto.LoginRequest;
import backend.VocaProject.user.dto.LoginResponse;
import backend.VocaProject.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static backend.VocaProject.response.BaseExceptionStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 유저 회원가입
     * @param request
     */
    @Transactional
    @Override
    public void userJoin(JoinRequest request) {
        // 로그인 아이디 중복 체크
        checkLoginIdDuplicate(request.getLoginId());

        String enPassword = bCryptPasswordEncoder.encode(request.getPassword());

        User user = new User(request.getUsername(), request.getLoginId(), enPassword);

        userRepository.save(user);
    }

    /**
     * 유저 로그인 아이디 중복 확인
     * @param loginId
     * @return
     */
    @Override
    public boolean checkLoginIdDuplicate(String loginId) {
        Optional<User> user = userRepository.findByLoginId(loginId);

        if (!user.isEmpty()) throw new BaseException(DUPLICATE_LOGIN_ID); // 중복O

        return false;
    }

    /**
     * loginId(String)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * loginId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * loginId로 찾아온 User가 존재하면 User return
     * @param loginId
     * @return
     */
    public User getLoginUserByLoginId(String loginId) {
        if (loginId == null) return null;

        Optional<User> user = userRepository.findByLoginId(loginId);
        if (user.isEmpty()) return null;

        return user.get();
    }

    /**
     * 유저 로그인
     * @param request
     * @return
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        // 로그인 아이디로 유저 있는지 확인
        User user = userRepository.findByLoginId(request.getLoginId()).orElseThrow(() -> new BaseException(LOGIN_USER_NOT_EXIST));

        // 비밀번호 맞는지 확인
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BaseException(LOGIN_USER_NOT_EXIST);
        }

        // 승인되지 않은 유저일 경우
        if (user.getApproval().equals("N")) throw new BaseException(WITHOUT_ACCESS_USER);

        String jwtToken = JwtTokenUtil.createToken(user.getLoginId());

        LoginResponse loginResponse = new LoginResponse(user.getId(), jwtToken, user.getRole());

        return loginResponse;
    }
}
