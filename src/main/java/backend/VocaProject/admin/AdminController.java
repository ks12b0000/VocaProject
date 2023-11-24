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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 API")
@RequestMapping("/api")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "유저 목록 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 목록 조회에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @GetMapping("/admin/users")
    public ResponseEntity userList(Authentication admin, @RequestParam String className) {
        List<UserListResponse> response = adminService.userList(admin, className);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "유저 목록 조회에 성공했습니다.", response));
    }

    @Operation(summary = "유저 승인X 목록 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 승인X 목록 조회에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @GetMapping("/admin/non-approval/users")
    public ResponseEntity userNonApprovalList() {
        List<UserListResponse> response = adminService.userNonApprovalList();

        return ResponseEntity.ok().body(new BaseResponse<>(200, "유저 승인X 목록 조회에 성공했습니다.", response));
    }

    @Operation(summary = "유저 승인 여부 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 승인 여부 변경에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @PatchMapping("/admin/users/approval")
    public ResponseEntity userApprovalUpdate(@RequestBody ApprovalUpdateRequest request) {
        adminService.userApprovalUpdate(request);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "유저 승인 여부 변경에 성공했습니다."));
    }

    @Operation(summary = "유저 정보 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 정보 변경에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @PatchMapping("/admin/users")
    public ResponseEntity userUpdate(Authentication admin, @RequestBody UserUpdateRequest request) {
        adminService.userUpdate(admin, request);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "유저 정보 변경에 성공했습니다."));
    }

    @Operation(summary = "유저 비밀번호 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 비밀번호 변경에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @PatchMapping("/admin/users/password")
    public ResponseEntity userPasswordUpdate(Authentication admin, @RequestBody UserUpdatePwRequest request) {
        adminService.userPasswordUpdate(admin, request);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "유저 비밀번호 변경에 성공했습니다."));
    }

    @Operation(summary = "유저 삭제 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 삭제에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @DeleteMapping("/master-admin/users/{userId}")
    public ResponseEntity userDelete(@PathVariable Long userId) {
        adminService.userDelete(userId);

        return ResponseEntity.ok(new BaseResponse(200, "유저 삭제에 성공했습니다."));
    }

    @Operation(summary = "단어장 삭제 API", responses = {
            @ApiResponse(responseCode = "200", description = "단어장 삭제에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @DeleteMapping("/master-admin/vocabulary-book/{categoryId}")
    public ResponseEntity vocabularyBookDelete(@PathVariable Long categoryId) {
        adminService.vocabularyBookDelete(categoryId);

        return ResponseEntity.ok().body(new BaseResponse(200, "단어장 삭제에 성공했습니다."));
    }

}
