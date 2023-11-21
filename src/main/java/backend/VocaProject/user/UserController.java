package backend.VocaProject.user;

import backend.VocaProject.domain.User;
import backend.VocaProject.jwt.dto.GeneratedToken;
import backend.VocaProject.response.BaseResponse;
import backend.VocaProject.response.ValidationSequence;
import backend.VocaProject.user.dto.JoinRequest;
import backend.VocaProject.user.dto.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "회원 API")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 API", responses = {
            @ApiResponse(responseCode = "201", description = "회원가입에 성공했습니다.")
    })
    @Tag(name = "Users")
    @PostMapping
    public ResponseEntity userJoin(@Validated(ValidationSequence.class) @RequestBody JoinRequest request) {

        userService.userJoin(request);

        return ResponseEntity.created(URI.create("/api/users" + request)).body(new BaseResponse(201, "회원가입에 성공했습니다."));
    }

    /**
     * [GET] /api/user/duplicate-check?loginId=
     * @param loginId
     * @return
     */
    @Operation(summary = "로그인 아이디 중복체크 API", responses = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 아이디 입니다.")
    })
    @Tag(name = "Users")
    @GetMapping("/duplicate-check")
    public ResponseEntity loginIdDuplicateCheck(@RequestParam String loginId) {
        boolean result = userService.checkLoginIdDuplicate(loginId);

        return ResponseEntity.ok(new BaseResponse(200, "사용 가능한 아이디 입니다.", result));
    }

    @Operation(summary = "로그인 API", responses = {
            @ApiResponse(responseCode = "200", description = "로그인에 성공했습니다.")
    })
    @Tag(name = "Users")
    @PostMapping("/login")
    public ResponseEntity login(@Validated(ValidationSequence.class) @RequestBody LoginRequest loginRequest) {
        GeneratedToken response = userService.login(loginRequest);

        return ResponseEntity.ok(new BaseResponse(200, "로그인에 성공했습니다.", response));
    }

}
