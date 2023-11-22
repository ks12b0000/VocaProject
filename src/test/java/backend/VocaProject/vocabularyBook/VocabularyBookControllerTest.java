package backend.VocaProject.vocabularyBook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class VocabularyBookControllerTest {

    @Autowired
    private MockMvc mvc;

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
}