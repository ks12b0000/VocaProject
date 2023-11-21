package backend.VocaProject.user;

import backend.VocaProject.domain.User;
import backend.VocaProject.jwt.RefreshTokenService;
import backend.VocaProject.jwt.dto.GeneratedToken;
import backend.VocaProject.response.BaseException;
import backend.VocaProject.user.dto.JoinRequest;
import backend.VocaProject.user.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static backend.VocaProject.jwt.JwtTokenUtil.generateAccessToken;
import static backend.VocaProject.jwt.JwtTokenUtil.generateRefreshToken;
import static backend.VocaProject.response.BaseExceptionStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RefreshTokenService tokenService;

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
     * 유저 로그인
     * @param request
     * @return
     */
    @Override
    public GeneratedToken login(LoginRequest request) {
        // 로그인 아이디로 유저 있는지 확인
        User user = userRepository.findByLoginId(request.getLoginId()).orElseThrow(() -> new BaseException(LOGIN_USER_NOT_EXIST));

        // 비밀번호 맞는지 확인
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BaseException(LOGIN_USER_NOT_EXIST);
        }

        // 승인되지 않은 유저일 경우
        if (user.getApproval().equals("N")) throw new BaseException(WITHOUT_ACCESS_USER);

        String refreshToken = generateRefreshToken(user.getLoginId(), user.getRole());
        String accessToken = generateAccessToken(user.getLoginId(), user.getRole());

        // 토큰을 Redis에 저장한다.
        tokenService.saveTokenInfo(user.getLoginId(), refreshToken, accessToken);

        return new GeneratedToken(accessToken, refreshToken);
    }
}
