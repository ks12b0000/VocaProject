package backend.VocaProject.vocabularyTest;

import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.user.UserRepository;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestResultRequest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class VocabularyTestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("테스트에 필요한 유저 저장")
    User beforeUser() {
        User user = new User("홍길동", "login1234", "password12");
        user.setClassName("중등 초급");
        user.setRole("ROLE_USER");
        Authentication authUser = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authUser);

        return user;
    }

    @DisplayName("단어 테스트 조회 성공")
    @Test
    void vocabularyBooks() throws Exception {
        // given
        User user = beforeUser();
        userRepository.save(user);
        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급2");
        long firstDay = 1L;
        long lastDay = 10L;

        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/vocabulary-test?categoryId=" + category.getId() +
                "&firstDay=" + firstDay + "&lastDay=" + lastDay));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("단어 테스트 조회에 성공했습니다."));
    }

    @DisplayName("단어 테스트 조회 실패")
    @Test
    void vocabularyBooksFail() throws Exception {
        // given
        User user = beforeUser();
        userRepository.save(user);
        VocabularyBookCategory category = new VocabularyBookCategory(121L, "중등 초급2");
        long firstDay = 1L;
        long lastDay = 10L;

        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/vocabulary-test?categoryId=" + category.getId() +
                "&firstDay=" + firstDay + "&lastDay=" + lastDay));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("400"))
                .andExpect(jsonPath("message").value("존재하지 않는 단어장입니다."));
    }

    @DisplayName("단어 테스트 결과 저장 성공")
    @Test
    void vocabularyTestResultSave() throws Exception {
        // given
        User user = beforeUser();
        userRepository.save(user);
        VocabularyTestResultRequest request = new VocabularyTestResultRequest(1L, 1, 10, "result", "record", null);
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/auth/vocabulary-test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));;

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("code").value("201"))
                .andExpect(jsonPath("message").value("단어 테스트 결과 저장에 성공했습니다."));
    }

    @DisplayName("단어 테스트 결과 저장 실패")
    @Test
    void vocabularyTestResultSaveFail() throws Exception {
        // given
        User user = beforeUser();
        userRepository.save(user);
        VocabularyTestResultRequest request = new VocabularyTestResultRequest(123L, 1, 10, "result", "record", null);
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/auth/vocabulary-test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));;

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("400"))
                .andExpect(jsonPath("message").value("존재하지 않는 단어장입니다."));
    }

    @DisplayName("단어 테스트 틀린 단어 조회 성공")
    @Test
    void vocabularyTestWrongWords() throws Exception {
        // given
        User user = beforeUser();
        userRepository.save(user);

        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/vocabulary-test/wrong/words"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("단어 테스트 틀린 단어 조회에 성공했습니다."));
    }
}