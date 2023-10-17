package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.UserListResponse;
import backend.VocaProject.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 API")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "유저 전체 목록 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 전체 목록 조회에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @GetMapping("/api/admin/user-list/{userId}")
    public BaseResponse userList(@PathVariable Long userId, @RequestParam String className, @RequestParam String approval) {
        List<UserListResponse> response = adminService.userList(userId, className, approval);

        return new BaseResponse("유저 전체 목록 조회에 성공했습니다.", response);
    }

}
