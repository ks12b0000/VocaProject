package backend.VocaProject.jwt;

import backend.VocaProject.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Tokens", description = "토큰 API")
public class AuthController {

    private final RefreshTokenRepository tokenRepository;
    private final RefreshTokenService tokenService;
    private final JwtTokenUtil jwtUtil;

    @Operation(summary = "로그아웃 API", responses = {
            @ApiResponse(responseCode = "200", description = "로그아웃에 성공했습니다.")
    })
    @Tag(name = "Tokens")
    @PostMapping("/api/token/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") final String accessToken) {

        // 엑세스 토큰으로 현재 Redis 정보 삭제
        tokenService.removeRefreshToken(accessToken);

        return ResponseEntity.ok(new BaseResponse(200, "로그아웃에 성공했습니다."));
    }

    @Operation(summary = "토큰 재생성 API", responses = {
            @ApiResponse(responseCode = "200", description = "새로운 AccessToken 발급에 성공했습니다.")
    })
    @Tag(name = "Tokens")
    @PostMapping("/api/token/refresh")
    public ResponseEntity refresh(@RequestHeader("Authorization") final String accessToken) {

        // 액세스 토큰으로 Refresh 토큰 객체를 조회
        Optional<RefreshToken> refreshToken = tokenRepository.findByAccessToken(accessToken);

        // RefreshToken이 존재하고 유효하다면 실행
        if (refreshToken.isPresent() && jwtUtil.verifyToken(refreshToken.get().getRefreshToken())) {
            // RefreshToken 객체를 꺼내온다.
            RefreshToken resultToken = refreshToken.get();
            // 권한과 아이디를 추출해 새로운 액세스토큰을 만든다.
            String newAccessToken = jwtUtil.generateAccessToken(resultToken.getId(), jwtUtil.getRole(resultToken.getRefreshToken()));
            // 액세스 토큰의 값을 수정해준다.
            resultToken.updateAccessToken(newAccessToken);
            tokenRepository.save(resultToken);
            // 새로운 액세스 토큰을 반환해준다.
            return ResponseEntity.ok(new BaseResponse<>(200, "새로운 AccessToken 발급에 성공했습니다.", newAccessToken));
        }

        return ResponseEntity.badRequest().body(new BaseResponse<>(400, "토큰 재발급에 실패했습니다."));
    }

}
