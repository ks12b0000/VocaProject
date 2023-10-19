package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.ApprovalUpdateRequest;
import backend.VocaProject.admin.dto.UserListResponse;
import backend.VocaProject.admin.dto.UserUpdateRequest;
import backend.VocaProject.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/api/admin/user-list/{adminId}")
    public BaseResponse userList(@PathVariable Long adminId, @RequestParam String className, @RequestParam String approval) {
        List<UserListResponse> response = adminService.userList(adminId, className, approval);

        return new BaseResponse("유저 전체 목록 조회에 성공했습니다.", response);
    }

    @Operation(summary = "유저 승인 여부 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 승인 여부 변경에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @PatchMapping("/api/master-admin/approval/user")
    public BaseResponse userApprovalUpdate(@RequestBody ApprovalUpdateRequest request) {
        adminService.userApprovalUpdate(request);

        return new BaseResponse("유저 승인 여부 변경에 성공했습니다.");
    }

    @Operation(summary = "유저 정보 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 정보 변경에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @PatchMapping("/api/admin/user-update/{adminId}")
    public BaseResponse userUpdate(@PathVariable Long adminId, @RequestBody UserUpdateRequest request) {
        adminService.userUpdate(adminId, request);

        return new BaseResponse("유저 정보 변경에 성공했습니다.");
    }

}
