package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.ApprovalUpdateRequest;
import backend.VocaProject.admin.dto.UserListResponse;
import backend.VocaProject.admin.dto.UserUpdateRequest;
import backend.VocaProject.domain.User;
import backend.VocaProject.user.UserRepository;
import backend.VocaProject.user.UserServiceImpl;
import backend.VocaProject.user.dto.JoinRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @DisplayName("테스트를 위한 유저 저장")
    User before() {
        // given
        JoinRequest request = new JoinRequest("홍길동", "example123", "example123");
        User user = new User(request.getUsername(), request.getLoginId(), request.getPassword());
        // stub
        when(userRepository.save(any())).thenReturn(user);
        // then
        userService.userJoin(request);
        return user;
    }

    @Test
    @DisplayName("마스터 관리자가 유저 목록 조회")
    void userList() {
        // given
        User user = before();
        user.setRole("ROLE_MASTER_ADMIN");
        user.setClassName("master");

        List<User> list = new ArrayList<>();
        list.add(user);

        // stub
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findByApproval("Y")).thenReturn(list);
        when(userRepository.findByApproval("N")).thenReturn(list);
        when(userRepository.findByClassNameAndApproval(any(), any())).thenReturn(list);

        // when
        // 마스터 관리자가 전체 승인 여부가 Y인 유저 전체 목록 조회
        List<UserListResponse> responses = adminService.userList(user.getId(), "all", "Y");
        // 마스터 관리자가 전체 승인 여부가 N인 유저 전체 목록 조회
        List<UserListResponse> responses2 = adminService.userList(user.getId(), "all", "N");
        // 마스터 관리자가 전체 승인 여부가 Y인 keyword에 맞는 클래스 유저 목록 조회
        List<UserListResponse> responses3 = adminService.userList(user.getId(), "중등 기초", "Y");

        // then
        assertAll(
                () -> assertThat(responses).isNotNull(),
                () -> assertThat(responses2).isNotNull(),
                () -> assertThat(responses3).isNotNull()
        );
    }

    @Test
    @DisplayName("중간 관리자가 유저 목록 조회")
    void userList2() {
        // given
        User user = before();
        user.setRole("ROLE_MIDDLE_ADMIN");
        user.setClassName("중등 기초");

        List<User> list = new ArrayList<>();
        list.add(user);

        // stub
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findByClassNameAndApproval(any(), any())).thenReturn(list);

        // when
        // 중간 관리자에 클래스별 승인 여부가 Y인 유저 목록 조회
        List<UserListResponse> responses = adminService.userList(user.getId(), null, null);

        // then
        assertThat(responses).isNotNull();
    }

    @Test
    @DisplayName("유저 승인 여부 변경")
    void userApproval() {
        // given
        User user = before();

        // stub
        when(userRepository.findByLoginId(user.getLoginId())).thenReturn(Optional.of(user));

        /**
         * 유저 승인 여부 Y로 변경
         */
        // when
        ApprovalUpdateRequest request = new ApprovalUpdateRequest(user.getLoginId(), "Y");
        adminService.userApprovalUpdate(request);
        // then
        assertThat(user.getApproval()).isEqualTo("Y");

        /**
         * 유저 승인 여부 N으로 변경
         */
        // when
        request = new ApprovalUpdateRequest(user.getLoginId(), "N");
        adminService.userApprovalUpdate(request);
        // then
        assertThat(user.getApproval()).isEqualTo("N");

    }

    @Test
    @DisplayName("마스터 관리자가 유저 정보 변경")
    void userUpdate() {
        // given
        User admin = before();
        admin.setRole("ROLE_MASTER_ADMIN");
        admin.setClassName("master");
        User user = before();

        // stub
        when(userRepository.findById(any())).thenReturn(Optional.of(admin));
        when(userRepository.findByLoginId(any())).thenReturn(Optional.of(user));

        // when
        UserUpdateRequest request = new UserUpdateRequest(user.getLoginId(), bCryptPasswordEncoder.encode("example123"), "ROLE_MIDDLE_USER", "중등 기초");

        adminService.userUpdate(admin.getId(), request);

        // then
        assertAll(
                () -> assertThat(request.getPassword()).isEqualTo(user.getPassword()),
                () -> assertThat(request.getRole()).isEqualTo(user.getRole()),
                () -> assertThat(request.getClassName()).isEqualTo(user.getClassName())
        );
    }
    @Test
    @DisplayName("중간 관리자가 유저 정보 변경")
    void userUpdate2() {
        // given
        User admin = before();
        admin.setRole("ROLE_MIDDLE_ADMIN");
        admin.setClassName("중등 기초");
        User user = before();
        user.setClassName("중등 기초");

        // stub
        when(userRepository.findById(any())).thenReturn(Optional.of(admin));
        when(userRepository.findByLoginId(any())).thenReturn(Optional.of(user));

        // when
        UserUpdateRequest request = new UserUpdateRequest(user.getLoginId(), null, null, "중등 기초2");

        adminService.userUpdate(admin.getId(), request);

        // then
        assertThat(request.getClassName()).isEqualTo(user.getClassName());

        /**
         * 중간 관리자가 맡은 클래스의 유저가 아닌 경우
         */
        user.setClassName("고등 기초");
        assertThatThrownBy(() -> adminService.userUpdate(admin.getId(), request)).hasMessage("권한이 없습니다. 관리자에게 문의하세요.");
    }
}
