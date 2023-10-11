package backend.VocaProject.user;

import backend.VocaProject.domain.User;
import backend.VocaProject.response.BaseResponse;
import backend.VocaProject.response.ValidationSequence;
import backend.VocaProject.user.dto.JoinRequest;
import backend.VocaProject.user.dto.LoginRequest;
import backend.VocaProject.user.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "회원 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 API", responses = {
            @ApiResponse(responseCode = "200", description = "회원가입에 성공했습니다.")
    })
    @Tag(name = "Users")
    @PostMapping("/api/user/join")
    public BaseResponse userJoin(@Validated(ValidationSequence.class) @RequestBody JoinRequest request) {

        userService.userJoin(request);

        return new BaseResponse("회원가입에 성공했습니다.");
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
    @GetMapping("/api/user/duplicate-check")
    public BaseResponse loginIdDuplicateCheck(@RequestParam String loginId) {
        boolean result = userService.checkLoginIdDuplicate(loginId);

        return new BaseResponse("사용 가능한 아이디 입니다.", result);
    }

    @Operation(summary = "로그인 API", responses = {
            @ApiResponse(responseCode = "200", description = "로그인에 성공했습니다.")
    })
    @Tag(name = "Users")
    @PostMapping("/api/user/login")
    public BaseResponse<LoginResponse> login(@Validated(ValidationSequence.class) @RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest);
        return new BaseResponse("로그인에 성공했습니다.", response);
    }

//    @GetMapping("/api/auth")
//    public User info(Authentication auth) {
//        User userInfo = userService.getLoginUserByLoginId(auth.getName());
//
//        return userInfo;
//    }
//
//    @Tag(name = "Users")
//    @GetMapping("/api/admin")
//    public String adminPage() {
//        return "관리자 페이지 접근 성공";
//    }
}
