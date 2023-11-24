package backend.VocaProject.vocabularyBook;

import backend.VocaProject.domain.User;
import backend.VocaProject.user.UserRepository;
import backend.VocaProject.vocabularyBook.dto.VocabularyLearningRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class VocabularyBookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("단어장 조회 성공")
    @Test
    @WithMockUser(roles = "USER")
    void vocabularyBooks() throws Exception {
        // given
        Long categoryId = 1L;
        int firstDay = 1;
        int lastDay = 20;

        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/vocabulary-book?categoryId=" + categoryId +
                "&firstDay=" + firstDay + "&lastDay=" + lastDay));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("단어장 조회에 성공했습니다."));
    }

    @DisplayName("단어장 조회 실패")
    @Test
    void vocabularyBooksFail() throws Exception {
        // given
        Long categoryId = 1L;
        int firstDay = 1;
        int lastDay = 20;

        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/vocabulary-book?categoryId=" + categoryId +
                "&firstDay=" + firstDay + "&lastDay=" + lastDay));

        // then
        resultActions.andExpect(status().isForbidden());
    }

    @DisplayName("단어장 학습 종료시 학습 저장 성공")
    @Test
    void vocabularyBookEndByLearning() throws Exception {
        // given
        User user = new User("홍길동", "login1234", "password12");
        user.setClassName("중등 초급");
        user.setRole("ROLE_USER");
        userRepository.save(user);
        Authentication authUser = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authUser);
        VocabularyLearningRequest request = new VocabularyLearningRequest(1L, 30L, 1, 2);
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/auth/vocabulary-book/learning")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("단어장 학습 저장에 성공했습니다."));
    }

    @DisplayName("단어장 학습 종료시 학습 저장 실패")
    @Test
    void vocabularyBookEndByLearningFail() throws Exception {
        // given
        User user = new User("홍길동", "login1234", "password12");
        user.setClassName("중등 초급");
        user.setRole("ROLE_USER");
        userRepository.save(user);
        Authentication authUser = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authUser);
        VocabularyLearningRequest request = new VocabularyLearningRequest(123L, 30L, 1, 2);
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/auth/vocabulary-book/learning")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("400"))
                .andExpect(jsonPath("message").value("존재하지 않는 단어장입니다."));
    }
}