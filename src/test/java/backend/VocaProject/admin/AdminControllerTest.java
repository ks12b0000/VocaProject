package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.ApprovalUpdateRequest;
import backend.VocaProject.admin.dto.UserUpdatePwRequest;
import backend.VocaProject.admin.dto.UserUpdateRequest;
import backend.VocaProject.admin.dto.VocabularyTestSettingRequest;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.user.UserRepository;
import backend.VocaProject.user.dto.JoinRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("테스트에 필요한 유저 저장")
    User before() {
        User user = new User("홍길동", "example0921", "example0921");
        userRepository.save(user);
        return user;
    }

    @Test
    @DisplayName("마스터 관리자가 유저 목록 조회")
    void userList() throws Exception {
        // given
        User user = before();
        user.setClassName("master");
        user.setRole("ROLE_MASTER_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // 마스터가 전체 승인 여부가 Y인 유저 전체 목록 조회
        ResultActions resultActions = mvc.perform(get("/api/admin/users?className=all"));
        // 마스터 관리자가 전체 승인 여부가 Y인 keyword에 맞는 클래스 유저 목록 조회
        ResultActions resultActions2 = mvc.perform(get("/api/admin/users?className=중등 기초"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("유저 목록 조회에 성공했습니다."));

        resultActions2.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("유저 목록 조회에 성공했습니다."));
    }

    @Test
    @DisplayName("중간 관리자가 유저 목록 조회")
    void userList2() throws Exception {
        // given
        User user = before();
        user.setClassName("중등 기초");
        user.setRole("ROLE_MIDDLE_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // 중간 관리자가 자기가 맡은 클래스의 승인 여부가 Y인 유저 목록만 조회
        ResultActions resultActions = mvc.perform(get("/api/admin/users?className="));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("유저 목록 조회에 성공했습니다."));
    }

    @Test
    @DisplayName("사용 승인X 유저 목록 조회")
    void userNonApprovalList() throws Exception {
        // given
        User user = before();
        user.setRole("ROLE_MASTER_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResultActions resultActions = mvc.perform(get("/api/admin/non-approval/users"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("유저 승인X 목록 조회에 성공했습니다."));
    }

    @Test
    @DisplayName("유저 승인 여부 변경")
    @WithMockUser(roles = "MASTER_ADMIN")
    void userApproval() throws Exception {
        // given
        User user = before();

        ApprovalUpdateRequest request = new ApprovalUpdateRequest(user.getId(), "Y", "ROLE_USER");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/admin/users/approval")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("유저 승인 여부 변경에 성공했습니다."));
    }

    @Test
    @DisplayName("마스터 관리자가 유저 정보 변경")
    void userUpdate() throws Exception {
        // given
        User adminUser = before();
        adminUser.setClassName("master");
        adminUser.setRole("ROLE_MASTER_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User("홍길동", "example00998f", "example0988231");
        userRepository.save(user);

        UserUpdateRequest request = new UserUpdateRequest(user.getId(), "ROLE_USER", "중등 기초");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("유저 정보 변경에 성공했습니다."));
    }

    @Test
    @DisplayName("중간 관리자가 유저 정보 변경")
    void userUpdate2() throws Exception {
        // given
        User adminUser = before();
        adminUser.setClassName("중등 기초");
        adminUser.setRole("ROLE_MIDDLE_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User("홍길동", "example00998f", "example0988231");
        userRepository.save(user);
        user.setClassName("중등 기초");

        UserUpdateRequest request = new UserUpdateRequest(user.getId(), "ROLE_USER", "중등 기초2");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("유저 정보 변경에 성공했습니다."));
    }

    @Test
    @DisplayName("중간 관리자가 유저 정보 변경 실패")
    void userUpdateFail() throws Exception {
        // given
        User adminUser = before();
        adminUser.setClassName("중등 기초");
        adminUser.setRole("ROLE_MIDDLE_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User("홍길동", "example00998f", "example0988231");
        userRepository.save(user);

        /**
         * 중간 관리자가 맡은 클래스의 유저가 아니면 변경 불가
         */
        UserUpdateRequest request = new UserUpdateRequest(user.getId(), "ROLE_USER", "중등 기초");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("403"))
                .andExpect(jsonPath("message").value("권한이 없습니다. 관리자에게 문의하세요."));
    }

    @Test
    @DisplayName("마스터 관리자가 유저 비밀번호 변경")
    void userPasswordUpdate() throws Exception {
        // given
        User adminUser = before();
        adminUser.setClassName("master");
        adminUser.setRole("ROLE_MASTER_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User("홍길동", "example00998f", "example0988231");
        userRepository.save(user);

        UserUpdatePwRequest request = new UserUpdatePwRequest(user.getId(), "update12");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/admin/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("유저 비밀번호 변경에 성공했습니다."));
    }

    @Test
    @DisplayName("중간 관리자가 유저 비밀번호 변경")
    void userPasswordUpdate2() throws Exception {
        // given
        User adminUser = before();
        adminUser.setClassName("중등 초급");
        adminUser.setRole("ROLE_MIDDLE_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User("홍길동", "example00998f", "example0988231");
        user.setClassName("중등 초급");
        userRepository.save(user);

        UserUpdatePwRequest request = new UserUpdatePwRequest(user.getId(), "update12");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/admin/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("유저 비밀번호 변경에 성공했습니다."));
    }

    @Test
    @DisplayName("중간 관리자가 유저 비밀번호 변경 실패")
    void userPasswordUpdateFail() throws Exception {
        // given
        User adminUser = before();
        adminUser.setClassName("중등 초급");
        adminUser.setRole("ROLE_MIDDLE_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User("홍길동", "example00998f", "example0988231");
        user.setClassName("고등 초급");
        userRepository.save(user);

        UserUpdatePwRequest request = new UserUpdatePwRequest(user.getId(), "update12");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/admin/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("403"))
                .andExpect(jsonPath("message").value("권한이 없습니다. 관리자에게 문의하세요."));
    }

    @Test
    @DisplayName("유저 삭제")
    @WithMockUser(roles = "MASTER_ADMIN")
    void userDelete() throws Exception {
        // given
        User user = before();

        // when
        ResultActions resultActions = mvc.perform(delete("/api/master-admin/users/" + user.getId()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("유저 삭제에 성공했습니다."));
    }

    @Test
    @DisplayName("단어장 삭제")
    @WithMockUser(roles = "MASTER_ADMIN")
    void vocabularyBookDelete() throws Exception {
        // given
        VocabularyBookCategory category = new VocabularyBookCategory(1L, "고등 기초");

        // when
        ResultActions resultActions = mvc.perform(delete("/api/master-admin/vocabulary-book/" + category.getId()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("단어장 삭제에 성공했습니다."));
    }

    @Test
    @DisplayName("마스터 관리자가 유저별 단어 테스트 목표 정답률 설정 성공")
    void vocabularyTestSetting() throws Exception {
        // given
        User adminUser = before();
        adminUser.setClassName("master");
        adminUser.setRole("ROLE_MASTER_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");

        User user = new User("홍길동", "example00998f", "example0988231");
        userRepository.save(user);

        VocabularyTestSettingRequest request = new VocabularyTestSettingRequest(user.getId(), category.getId(), 90, 1, 2);
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/admin/vocabulary-book/test/setting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("code").value("201"))
                .andExpect(jsonPath("message").value("유저별 단어 테스트 설정에 성공했습니다."));
    }

    @Test
    @DisplayName("중간 관리자가 유저별 단어 테스트 목표 정답률 설정 성공")
    void vocabularyTestSetting2() throws Exception {
        // given
        User adminUser = before();
        adminUser.setClassName("중등 초급");
        adminUser.setRole("ROLE_MIDDLE_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");

        User user = new User("홍길동", "example00998f", "example0988231");
        user.setClassName("중등 초급");
        userRepository.save(user);

        VocabularyTestSettingRequest request = new VocabularyTestSettingRequest(user.getId(), category.getId(), 90, 1, 2);
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/admin/vocabulary-book/test/setting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("code").value("201"))
                .andExpect(jsonPath("message").value("유저별 단어 테스트 설정에 성공했습니다."));
    }

    @Test
    @DisplayName("중간 관리자가 유저별 단어 테스트 목표 정답률 설정 실패")
    void vocabularyTestSettingFail() throws Exception {
        // given
        User adminUser = before();
        adminUser.setClassName("중등 초급");
        adminUser.setRole("ROLE_MIDDLE_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminUser, "",
                List.of(new SimpleGrantedAuthority(adminUser.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");

        User user = new User("홍길동", "example00998f", "example0988231");
        userRepository.save(user);

        VocabularyTestSettingRequest request = new VocabularyTestSettingRequest(user.getId(), category.getId(), 90, 1, 2);
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/admin/vocabulary-book/test/setting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("403"))
                .andExpect(jsonPath("message").value("권한이 없습니다. 관리자에게 문의하세요."));
    }

    @Test
    @DisplayName("마스터 관리자가 단어 테스트 결과 목록 조회")
    void vocabularyTestResultLists() throws Exception {
        // given
        User user = before();
        user.setClassName("master");
        user.setRole("ROLE_MASTER_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResultActions resultActions = mvc.perform(get("/api/admin/vocabulary-book/test/result/list"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("단어 테스트 결과 목록 조회에 성공했습니다."));
    }

    @Test
    @DisplayName("중간 관리자가 단어 테스트 결과 목록 조회")
    void vocabularyTestResultLists2() throws Exception {
        // given
        User user = before();
        user.setClassName("중등 기초");
        user.setRole("ROLE_MIDDLE_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResultActions resultActions = mvc.perform(get("/api/admin/vocabulary-book/test/result/list"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("단어 테스트 결과 목록 조회에 성공했습니다."));
    }
}
