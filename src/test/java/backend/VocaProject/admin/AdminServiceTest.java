package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.*;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.user.UserRepository;
import backend.VocaProject.user.UserServiceImpl;
import backend.VocaProject.user.dto.JoinRequest;
import backend.VocaProject.vocabularyBook.VocabularyBookRepository;
import backend.VocaProject.vocabularyBookCategory.VocabularyBookCategoryRepository;
import backend.VocaProject.vocabularyTest.VocabularyTestCustomRepositoryImpl;
import backend.VocaProject.vocabularyTest.VocabularyTestRepository;
import backend.VocaProject.vocabularyTestSetting.VocabularyTestSettingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private VocabularyBookCategoryRepository categoryRepository;

    @Mock
    private VocabularyTestSettingRepository vocabularyTestSettingRepository;

    @Mock
    private VocabularyBookRepository vocabularyBookRepository;

    @Mock
    private VocabularyTestRepository vocabularyTestRepository;

    @Mock
    private VocabularyTestCustomRepositoryImpl vocabularyTestCustomRepository;


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
        user.setClassName("master");
        user.setRole("ROLE_MASTER_ADMIN");
        Authentication admin = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(admin);
        List<User> list = new ArrayList<>();
        list.add(user);

        // stub
        when(userRepository.findByClassNameAndApproval(any(), any())).thenReturn(list);

        // when
        // 마스터 관리자가 전체 승인 여부가 Y인 유저 전체 목록 조회
        List<UserListResponse> responses = adminService.userList(admin, "all");

        // 마스터 관리자가 전체 승인 여부가 Y인 keyword에 맞는 클래스 유저 목록 조회
        List<UserListResponse> responses2 = adminService.userList(admin, "중등 기초");

        // then
        assertAll(
                () -> assertThat(responses).isNotNull(),
                () -> assertThat(responses2).isNotNull()
        );
    }

    @Test
    @DisplayName("중간 관리자가 유저 목록 조회")
    void userList2() {
        // given
        User user = before();
        user.setClassName("중등 기초");
        user.setRole("ROLE_MIDDLE_ADMIN");
        Authentication admin = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(admin);

        List<User> list = new ArrayList<>();
        list.add(user);

        // stub
        when(userRepository.findByClassNameAndApproval(any(), any())).thenReturn(list);

        // when
        // 중간 관리자에 클래스별 승인 여부가 Y인 유저 목록 조회
        List<UserListResponse> responses = adminService.userList(admin, null);

        // then
        assertThat(responses).isNotNull();
    }

    @Test
    @DisplayName("사용 승인X 유저 목록 조회")
    void userNonApprovalList() {
        // given
        // when
        List<UserListResponse> responses = adminService.userNonApprovalList();

        // then
        assertThat(responses).isNotNull();
    }

    @Test
    @DisplayName("유저 승인 여부 변경")
    void userApproval() {
        // given
        User user = before();

        // stub
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        /**
         * 유저 승인 여부 Y로 변경
         */
        // when
        ApprovalUpdateRequest request = new ApprovalUpdateRequest(user.getId(), "Y", "ROLE_USER");
        adminService.userApprovalUpdate(request);
        // then
        assertThat(user.getApproval()).isEqualTo("Y");

        /**
         * 유저 승인 여부 N으로 변경
         */
        // when
        request = new ApprovalUpdateRequest(user.getId(), "N", "ROLE_NULL");
        adminService.userApprovalUpdate(request);
        // then
        assertThat(user.getApproval()).isEqualTo("N");

    }

    @Test
    @DisplayName("마스터 관리자가 유저 정보 변경")
    void userUpdate() {
        // given
        User adminUser = before();
        adminUser.setClassName("master");
        adminUser.setRole("ROLE_MASTER_ADMIN");
        Authentication admin = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(admin);
        User user = before();

        // stub
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        UserUpdateRequest request = new UserUpdateRequest(user.getId(), "ROLE_MIDDLE_USER", "중등 기초");

        adminService.userUpdate(admin, request);

        // then
        assertAll(
                () -> assertThat(request.getRole()).isEqualTo(user.getRole()),
                () -> assertThat(request.getClassName()).isEqualTo(user.getClassName())
        );
    }

    @Test
    @DisplayName("중간 관리자가 유저 정보 변경")
    void userUpdate2() {
        // given
        User adminUser = before();
        adminUser.setClassName("중등 기초");
        adminUser.setRole("ROLE_MIDDLE_ADMIN");
        Authentication admin = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(admin);
        User user = before();
        user.setClassName("중등 기초");

        // stub
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        UserUpdateRequest request = new UserUpdateRequest(user.getId(), null, "중등 기초2");

        adminService.userUpdate(admin, request);

        // then
        assertThat(request.getClassName()).isEqualTo(user.getClassName());

        /**
         * 중간 관리자가 맡은 클래스의 유저가 아닌 경우
         */
        user.setClassName("고등 기초");
        assertThatThrownBy(() -> adminService.userUpdate(admin, request)).hasMessage("권한이 없습니다. 관리자에게 문의하세요.");
    }

    @Test
    @DisplayName("마스터 관리자가 유저 비밀번호 변경")
    void userPasswordUpdate() {
        // given
        User adminUser = before();
        adminUser.setClassName("master");
        adminUser.setRole("ROLE_MASTER_ADMIN");
        Authentication admin = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(admin);
        User user = before();

        // stub
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        UserUpdatePwRequest request = new UserUpdatePwRequest(user.getId(), "update12");
        adminService.userPasswordUpdate(admin, request);

        // then
        assertTrue(bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword()));
    }

    @Test
    @DisplayName("중간 관리자가 유저 비밀번호 변경")
    void userPasswordUpdate2() {
        // given
        User adminUser = before();
        adminUser.setClassName("중등 초급");
        adminUser.setRole("ROLE_MIDDLE_ADMIN");
        Authentication admin = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(admin);
        User user = before();
        user.setClassName("중등 초급");

        // stub
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        UserUpdatePwRequest request = new UserUpdatePwRequest(user.getId(), "update12");
        adminService.userPasswordUpdate(admin, request);

        // then
        assertTrue(bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword()));
    }

    @Test
    @DisplayName("중간 관리자가 유저 비밀번호 변경 실패")
    void userPasswordUpdateFail() {
        // given
        User adminUser = before();
        adminUser.setClassName("중등 초급");
        adminUser.setRole("ROLE_MIDDLE_ADMIN");
        Authentication admin = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(admin);
        User user = before();
        user.setClassName("중등 중급");

        // stub
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        UserUpdatePwRequest request = new UserUpdatePwRequest(user.getId(), "update12");

        // then
        assertThatThrownBy(() -> adminService.userPasswordUpdate(admin, request)).hasMessage("권한이 없습니다. 관리자에게 문의하세요.");
    }

    @Test
    @DisplayName("유저 삭제")
    void userDelete() {
        // given
        User user = before();

        // stub
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // when
        adminService.userDelete(user.getId());

        when(userRepository.findById(user.getId())).thenReturn(null);
        // then
        assertThat(userRepository.findById(user.getId())).isNull();
    }

    @Test
    @DisplayName("단어장 삭제")
    void vocabularyBookDelete() {
        // given
        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");

        // stub
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        // when
        adminService.vocabularyBookDelete(category.getId());

        // then
        assertThat(vocabularyBookRepository.findByVocabularyBookCategory(category)).isEmpty();
    }

    @Test
    @DisplayName("마스터 관리자가 유저별 단어 테스트 목표 정답률 설정 성공")
    void vocabularyTestSetting() {
        // given
        User adminUser = before();
        adminUser.setClassName("master");
        adminUser.setRole("ROLE_MASTER_ADMIN");
        Authentication admin = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(admin);
        User user = before();
        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");

        // stub
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        // when
        VocabularyTestSettingRequest request = new VocabularyTestSettingRequest(user.getId(), category.getId(), 90, 1, 2);
        adminService.vocabularyTestSetting(admin, request);
        // then

    }

    @Test
    @DisplayName("중간 관리자가 유저별 단어 테스트 목표 정답률 설정 성공")
    void vocabularyTestSetting2() {
        // given
        User adminUser = before();
        adminUser.setClassName("중등 초급");
        adminUser.setRole("ROLE_MIDDLE_ADMIN");
        Authentication admin = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(admin);
        User user = before();
        user.setClassName("중등 초급");
        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");

        // stub
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        // when
        VocabularyTestSettingRequest request = new VocabularyTestSettingRequest(user.getId(), category.getId(), 90, 1, 2);
        adminService.vocabularyTestSetting(admin, request);
        // then

    }
    @Test
    @DisplayName("중간 관리자가 유저별 단어 테스트 목표 정답률 설정 실패")
    void vocabularyTestSettingFail2() {
        // given
        User adminUser = before();
        adminUser.setClassName("중등 초급");
        adminUser.setRole("ROLE_MIDDLE_ADMIN");
        Authentication admin = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(admin);
        User user = before();
        user.setClassName("중등 중급");
        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");

        // stub
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        // when
        VocabularyTestSettingRequest request = new VocabularyTestSettingRequest(user.getId(), category.getId(), 90, 1, 2);

        // then
        assertThatThrownBy(() -> adminService.vocabularyTestSetting(admin, request)).hasMessage("권한이 없습니다. 관리자에게 문의하세요.");
    }

    @Test
    @DisplayName("마스터 관리자가 단어 테스트 결과 목록 조회")
    void vocabularyTestResultLists() {
        // given
        User adminUser = before();
        adminUser.setClassName("master");
        adminUser.setRole("ROLE_MASTER_ADMIN");
        Authentication admin = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(admin);
        int size = 10;
        LocalDateTime lastModifiedAt = LocalDateTime.parse("2024-01-14T14:20");

        // when
        List<VocabularyTestResultListResponse> responses = adminService.vocabularyTestResultLists(admin, size, lastModifiedAt);

        // then
        assertThat(responses).isNotNull();
    }

    @Test
    @DisplayName("중간 관리자가 단어 테스트 결과 목록 조회")
    void vocabularyTestResultLists2() {
        // given
        User adminUser = before();
        adminUser.setClassName("중등 초급");
        adminUser.setRole("ROLE_MIDDLE_ADMIN");
        Authentication admin = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(admin);
        int size = 10;
        LocalDateTime lastModifiedAt = LocalDateTime.parse("2024-01-14T14:20");

        // when
        List<VocabularyTestResultListResponse> responses = adminService.vocabularyTestResultLists(admin, size, lastModifiedAt);

        // then
        assertThat(responses).isNotNull();
    }

}
