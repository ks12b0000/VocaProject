package backend.VocaProject.jwt;

import backend.VocaProject.jwt.dto.GeneratedToken;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
public class JwtTokenUtil {

    @Value("${JWT_SECRET_KEY}")
    public void setJwtSecretKey(String jwtSecretKey) {
        JWT_SECRET_KEY = jwtSecretKey;
    }

    private static String JWT_SECRET_KEY;
    private static long ACCESS_TOKEN_EXPIRE_MILLIS = Duration.ofMinutes(60).toMillis();
    private static long REFRESH_TOKEN_EXPIRE_MILLIS = Duration.ofDays(30).toMillis();

    public static String generateRefreshToken(String loginId, String role) {
        // 새로운 클레임 객체를 생성하고, 이메일과 역할(권한)을 셋팅
        Claims claims = Jwts.claims().setSubject(loginId);
        claims.put("role", role);

        // 현재 시간과 날짜를 가져온다.
        Date now = new Date();

        return Jwts.builder()
                // Payload를 구성하는 속성들을 정의한다.
                .setClaims(claims)
                // 발행일자를 넣는다.
                .setIssuedAt(now)
                // 토큰의 만료일시를 설정한다.
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_MILLIS))
                // 지정된 서명 알고리즘과 비밀 키를 사용하여 토큰을 서명한다.
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }


    public static String generateAccessToken(String loginId, String role) {
        Claims claims = Jwts.claims().setSubject(loginId);
        claims.put("role", role);

        Date now = new Date();
        return
                Jwts.builder()
                        // Payload를 구성하는 속성들을 정의한다.
                        .setClaims(claims)
                        // 발행일자를 넣는다.
                        .setIssuedAt(now)
                        // 토큰의 만료일시를 설정한다.
                        .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_MILLIS))
                        // 지정된 서명 알고리즘과 비밀 키를 사용하여 토큰을 서명한다.
                        .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                        .compact();

    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY) // 비밀키를 설정하여 파싱한다.
                    .parseClaimsJws(token);  // 주어진 토큰을 파싱하여 Claims 객체를 얻는다.
            // 토큰의 만료 시간과 현재 시간비교
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());  // 만료 시간이 현재 시간 이후인지 확인하여 유효성 검사 결과를 반환
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserid(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰에서 ROLE(권한)만 추출한다.
    public String getRole(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody().get("role", String.class);
    }
}
