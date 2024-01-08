package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.*;
import backend.VocaProject.response.BaseResponse;
import backend.VocaProject.response.ValidationSequence;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity userApprovalUpdate(@Validated(ValidationSequence.class) @RequestBody ApprovalUpdateRequest request) {
        adminService.userApprovalUpdate(request);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "유저 승인 여부 변경에 성공했습니다."));
    }

    @Operation(summary = "유저 정보 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 정보 변경에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @PatchMapping("/admin/users")
    public ResponseEntity userUpdate(Authentication admin, @Validated(ValidationSequence.class) @RequestBody UserUpdateRequest request) {
        adminService.userUpdate(admin, request);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "유저 정보 변경에 성공했습니다."));
    }

    @Operation(summary = "유저 비밀번호 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 비밀번호 변경에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @PatchMapping("/admin/users/password")
    public ResponseEntity userPasswordUpdate(Authentication admin, @Validated(ValidationSequence.class) @RequestBody UserUpdatePwRequest request) {
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

        return ResponseEntity.ok().body(new BaseResponse(200, "유저 삭제에 성공했습니다."));
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

    @Operation(summary = "유저별 단어 테스트 설정 API", responses = {
            @ApiResponse(responseCode = "201", description = "유저별 단어 테스트 설정에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @PostMapping("/admin/vocabulary-book/test/setting")
    public ResponseEntity vocabularyTestSetting(Authentication admin, @Validated(ValidationSequence.class) @RequestBody VocabularyTestSettingRequest request) {
        adminService.vocabularyTestSetting(admin, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(201, "유저별 단어 테스트 설정에 성공했습니다."));
    }

    @Operation(summary = "단어 테스트 결과 목록 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "단어 테스트 결과 목록 조회에 성공했습니다.")
    })
    @Tag(name = "Admin")
    @GetMapping("/admin/vocabulary-book/test/result/list")
    public ResponseEntity vocabularyTestResultLists(Authentication admin, @PageableDefault(size = 10, sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        List<VocabularyTestResultListResponse> response = adminService.vocabularyTestResultLists(admin, pageable);

        return ResponseEntity.ok().body(new BaseResponse(200, "단어 테스트 결과 목록 조회에 성공했습니다.", response));
    }

}
