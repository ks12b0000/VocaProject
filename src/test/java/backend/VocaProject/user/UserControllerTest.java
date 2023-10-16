package backend.VocaProject.user;

import backend.VocaProject.domain.User;
import backend.VocaProject.user.dto.JoinRequest;
import backend.VocaProject.user.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("테스트에 필요한 유저 저장")
    public void before() throws Exception {
        // given
        JoinRequest request = new JoinRequest("홍길동", "example1", "example123");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        mvc.perform(post("/api/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );
    }

    @Test
    @DisplayName("유저 회원가입")
    void join() throws Exception {
        // given
        JoinRequest request = new JoinRequest("홍길동", "example1243", "example123");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("1000"))
                .andExpect(jsonPath("message").value("회원가입에 성공했습니다."));
    }

    @Test
    @DisplayName("유저 회원가입 실패")
    void joinFail() throws Exception {
        // 테스트 유저 저장
        before();

        /**
         * 로그인 아이디 중복
         */
        // given
        JoinRequest request = new JoinRequest("홍길동", "example1", "example123");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("3001"))
                .andExpect(jsonPath("message").value("중복된 아이디가 있습니다."));
    }

    @Test
    @DisplayName("로그인 아이디 중복 확인")
    void checkLoginIdDuplicate() throws Exception {
        // 테스트 유저 저장
        before();

        // given
        String loginId = "example12";
        String duplicateLoginId = "example1";

        // when
        ResultActions resultActions = mvc.perform(get("/api/user/duplicate-check?loginId=" + loginId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // 로그인 아이디 중복O
        ResultActions resultActions2 = mvc.perform(get("/api/user/duplicate-check?loginId=" + duplicateLoginId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("1000"))
                .andExpect(jsonPath("message").value("사용 가능한 아이디 입니다."));

        resultActions2.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("3001"))
                .andExpect(jsonPath("message").value("중복된 아이디가 있습니다."));
    }

    @Test
    @DisplayName("유저 로그인 Approval = N일 경우")
    void loginFail() throws Exception {
        // 테스트 유저 저장
        before();

        // given
        LoginRequest request = new LoginRequest("example1", "example123");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("3002"))
                .andExpect(jsonPath("message").value("권한이 없습니다. 관리자에게 문의하세요."));

    }

    @Test
    @DisplayName("유저 로그인 Approval = Y일 경우")
    void login() throws Exception {
        // given
        // 데이터 미리 저장해둠
        LoginRequest request = new LoginRequest("example000", "example000");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("1000"))
                .andExpect(jsonPath("message").value("로그인에 성공했습니다."));

    }

}
