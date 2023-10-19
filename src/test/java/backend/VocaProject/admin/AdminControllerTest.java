package backend.VocaProject.admin;

import backend.VocaProject.admin.dto.ApprovalUpdateRequest;
import backend.VocaProject.admin.dto.UserUpdateRequest;
import backend.VocaProject.domain.User;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

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
    @WithMockUser(username = "example0930", roles = "MASTER_ADMIN")
    void userList() throws Exception {
        // given
        // when
        // 마스터가 전체 승인 여부가 Y인 유저 전체 목록 조회
        ResultActions resultActions = mvc.perform(get("/api/admin/user-list/2?className=all&approval=Y"));
        // 마스터 관리자가 전체 승인 여부가 N인 유저 전체 목록 조회
        ResultActions resultActions2 = mvc.perform(get("/api/admin/user-list/2?className=all&approval=N"));
        // 마스터 관리자가 전체 승인 여부가 Y인 keyword에 맞는 클래스 유저 목록 조회
        ResultActions resultActions3 = mvc.perform(get("/api/admin/user-list/2?className=중등 기초&approval=Y"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("1000"))
                .andExpect(jsonPath("message").value("유저 전체 목록 조회에 성공했습니다."));

        resultActions2.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("1000"))
                .andExpect(jsonPath("message").value("유저 전체 목록 조회에 성공했습니다."));

        resultActions3.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("1000"))
                .andExpect(jsonPath("message").value("유저 전체 목록 조회에 성공했습니다."));
    }

    @Test
    @DisplayName("중간 관리자가 유저 목록 조회")
    @WithMockUser(username = "example09302", roles = "MIDDLE_ADMIN")
    void userList2() throws Exception {
        // given
        // when
        // 중간 관리자가 자기가 맡은 클래스의 승인 여부가 Y인 유저 목록만 조회
        ResultActions resultActions = mvc.perform(get("/api/admin/user-list/2?className=&approval="));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("1000"))
                .andExpect(jsonPath("message").value("유저 전체 목록 조회에 성공했습니다."));
    }

    @Test
    @DisplayName("유저 승인 여부 변경")
    @WithMockUser(username = "example0930", roles = "MASTER_ADMIN")
    void userApproval() throws Exception {
        /**
         * 유저 저장
         */
        User user = before();

        // given
        ApprovalUpdateRequest request = new ApprovalUpdateRequest(user.getLoginId(), "Y");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/master-admin/approval/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("1000"))
                .andExpect(jsonPath("message").value("유저 승인 여부 변경에 성공했습니다."));
    }

    @Test
    @DisplayName("마스터 관리자가 유저 정보 변경")
    @WithMockUser(username = "example0933", roles = "MASTER_ADMIN")
    void userUpdate() throws Exception {
        /**
         * 마스터 관리자 저장
         */
        User admin = before();
        admin.setClassName("master");

        /**
         * 유저 저장
         */
        User user = new User("홍길동", "example00998f", "example0988231");
        userRepository.save(user);

        // given
        UserUpdateRequest request = new UserUpdateRequest(user.getLoginId(), "example123", "ROLE_USER", "중등 기초");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/admin/user-update/" + admin.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("1000"))
                .andExpect(jsonPath("message").value("유저 정보 변경에 성공했습니다."));
    }

    @Test
    @DisplayName("중간 관리자가 유저 정보 변경")
    @WithMockUser(username = "example0933", roles = "MIDDLE_ADMIN")
    void userUpdate2() throws Exception {
        /**
         * 중간 관리자 저장
         */
        User admin = before();
        admin.setClassName("중등 기초");

        /**
         * 유저 저장
         */
        User user = new User("홍길동", "example00998f", "example0988231");
        userRepository.save(user);
        user.setClassName("중등 기초");

        // given
        UserUpdateRequest request = new UserUpdateRequest(user.getLoginId(), null, null, "중등 기초2");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/admin/user-update/" + admin.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("1000"))
                .andExpect(jsonPath("message").value("유저 정보 변경에 성공했습니다."));
    }

    @Test
    @DisplayName("중간 관리자가 유저 정보 변경 실패")
    @WithMockUser(username = "example0933", roles = "MIDDLE_ADMIN")
    void userUpdateFail() throws Exception {
        /**
         * 중간 관리자 저장
         */
        User admin = before();
        admin.setClassName("중등 기초");

        /**
         * 유저 저장
         */
        User user = new User("홍길동", "example00998f", "example0988231");
        userRepository.save(user);

        /**
         * 중간 관리자가 맡은 클래스의 유저가 아니면 변경 불가
         */
        // given
        UserUpdateRequest request = new UserUpdateRequest(user.getLoginId(), null, null, "중등 기초");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/admin/user-update/" + admin.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("3003"))
                .andExpect(jsonPath("message").value("권한이 없습니다. 관리자에게 문의하세요."));
    }
}
