package backend.VocaProject.jwt;

import backend.VocaProject.domain.User;
import backend.VocaProject.user.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().contains("/api/token/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request Header에서 AccessToken을 가져온다.
        String atc = request.getHeader("Authorization");

        // 토큰 검사 생략(모두 허용 URL의 경우 토큰 검사 통과)
        if (!StringUtils.hasText(atc)) {
            doFilter(request, response, filterChain);
            return;
        }

        // AccessToken을 검증하고, 만료되었을경우 예외를 발생시킨다.
        if (!jwtTokenUtil.verifyToken(atc)) {
            throw new JwtException("Access Token 만료!");
        }

        // AccessToken의 값이 있고, 유효한 경우에 진행한다.
        if (jwtTokenUtil.verifyToken(atc)) {

            // AccessToken 내부의 payload에 있는 loginId로 user를 조회한다. 없다면 예외를 발생시킨다 -> 정상 케이스가 아님
            User findUser = userRepository.findByLoginId(jwtTokenUtil.getUserid(atc))
                    .orElseThrow(IllegalStateException::new);

            // SecurityContext에 인증 객체를 등록해준다.
            Authentication auth = getAuthentication(findUser);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    public Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
    }

}
