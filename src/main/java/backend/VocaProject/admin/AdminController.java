package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.ApprovalUpdateRequest;
import backend.VocaProject.admin.dto.UserListResponse;
import backend.VocaProject.admin.dto.UserUpdatePwRequest;
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

    @Operation(summary = "유저 목록 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 목록 조회에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @GetMapping("/api/admin/users")
    public BaseResponse userList(@RequestParam Long adminId, @RequestParam String className) {
        List<UserListResponse> response = adminService.userList(adminId, className);

        return new BaseResponse("유저 목록 조회에 성공했습니다.", response);
    }

    @Operation(summary = "유저 승인X 목록 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 승인X 목록 조회에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @GetMapping("/api/admin/non-approval/users")
    public BaseResponse userNonApprovalList() {
        List<UserListResponse> response = adminService.userNonApprovalList();

        return new BaseResponse("유저 승인X 목록 조회에 성공했습니다.", response);
    }

    @Operation(summary = "유저 승인 여부 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 승인 여부 변경에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @PatchMapping("/api/admin/user/approval")
    public BaseResponse userApprovalUpdate(@RequestBody ApprovalUpdateRequest request) {
        adminService.userApprovalUpdate(request);

        return new BaseResponse("유저 승인 여부 변경에 성공했습니다.");
    }

    @Operation(summary = "유저 정보 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 정보 변경에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @PatchMapping("/api/admin/user")
    public BaseResponse userUpdate(@RequestParam Long adminId, @RequestBody UserUpdateRequest request) {
        adminService.userUpdate(adminId, request);

        return new BaseResponse("유저 정보 변경에 성공했습니다.");
    }

    @Operation(summary = "유저 비밀번호 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 비밀번호 변경에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @PatchMapping("/api/admin/user/password")
    public BaseResponse userPasswordUpdate(@RequestParam Long adminId, @RequestParam String userLoginId, @RequestBody UserUpdatePwRequest request) {
        adminService.userPasswordUpdate(adminId, userLoginId, request);

        return new BaseResponse("유저 비밀번호 변경에 성공했습니다.");
    }

    @Operation(summary = "유저 삭제 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 삭제에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @DeleteMapping("/api/master-admin/user")
    public BaseResponse userDelete(@RequestParam String userLoginId) {
        adminService.userDelete(userLoginId);

        return new BaseResponse("유저 삭제에 성공했습니다.");
    }

    @Operation(summary = "단어장 삭제 API", responses = {
            @ApiResponse(responseCode = "200", description = "단어장 삭제에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @DeleteMapping("/api/master-admin/vocabulary-book")
    public BaseResponse vocabularyBookDelete(@RequestParam Long categoryId) {
        adminService.vocabularyBookDelete(categoryId);

        return new BaseResponse("단어장 삭제에 성공했습니다.");
    }

}
