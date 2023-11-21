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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("테스트에 필요한 유저 저장")
    User before() {
        User user = new User("홍길동", "example0921", bCryptPasswordEncoder.encode("example0921"));
        userRepository.save(user);
        return user;
    }

    @Test
    @DisplayName("유저 회원가입")
    void join() throws Exception {
        // given
        JoinRequest request = new JoinRequest("홍길동", "example124333f2", "example123");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("code").value("201"))
                .andExpect(jsonPath("message").value("회원가입에 성공했습니다."));
    }

    @Test
    @DisplayName("유저 회원가입 실패")
    void joinFail() throws Exception {
        /**
         * 유저 저장
         */
        User user = before();

        /**
         * 로그인 아이디 중복
         */
        // given
        JoinRequest request = new JoinRequest("홍길동", user.getLoginId(), "example123");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("code").value("409"))
                .andExpect(jsonPath("message").value("중복된 아이디가 있습니다."));
    }

    @Test
    @DisplayName("로그인 아이디 중복 확인")
    void checkLoginIdDuplicate() throws Exception {
        /**
         * 유저 저장
         */
        User user = before();

        // given
        String loginId = "example12";
        String duplicateLoginId = "example0921";

        // when
        ResultActions resultActions = mvc.perform(get("/api/users/duplicate-check?loginId=" + loginId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // 로그인 아이디 중복O
        ResultActions resultActions2 = mvc.perform(get("/api/users/duplicate-check?loginId=" + duplicateLoginId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("사용 가능한 아이디 입니다."));

        resultActions2.andExpect(status().isConflict())
                .andExpect(jsonPath("code").value("409"))
                .andExpect(jsonPath("message").value("중복된 아이디가 있습니다."));
    }

    @Test
    @DisplayName("유저 로그인 Approval = N일 경우")
    void loginFail() throws Exception {
        /**
         * 유저 저장
         */
        User user = before();

        // given
        LoginRequest request = new LoginRequest(user.getLoginId(), "example0921");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/users/login")
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
    @DisplayName("유저 로그인 Approval = Y일 경우")
    void login() throws Exception {
        /**
         * 유저 저장
         */
        User user = before();
        user.setApproval("Y");

        // given
        LoginRequest request = new LoginRequest("example0921", "example0921");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("로그인에 성공했습니다."));

    }

}
