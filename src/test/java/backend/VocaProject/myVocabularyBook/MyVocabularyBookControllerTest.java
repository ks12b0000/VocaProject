package backend.VocaProject.myVocabularyBook;

import backend.VocaProject.domain.MyVocabularyBook;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBook;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.user.UserRepository;
import backend.VocaProject.vocabularyBook.VocabularyBookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MyVocabularyBookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private VocabularyBookRepository vocabularyBookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyVocabularyBookRepository myVocabularyBookRepository;

    @DisplayName("테스트에 필요한 유저 저장")
    Authentication beforeUser() {
        User user = new User("홍길동", "login1234", "password12");
        user.setClassName("중등 초급");
        user.setRole("ROLE_USER");
        Authentication authUser = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authUser);

        return authUser;
    }

    @DisplayName("테스트에 필요한 단어장 추가")
    VocabularyBook beforeVocabularyBook() {
        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");
        VocabularyBook vocabularyBook = new VocabularyBook("hi", "안녕", 1, category);

        return vocabularyBook;
    }

    @DisplayName("나만의 단어장에 단어 추가 성공")
    @Test
    void myVocabularyBookInsert() throws Exception {
        // given
        Authentication authentication = beforeUser();
        User user = (User) authentication.getPrincipal();
        userRepository.save(user);
        VocabularyBook vocabularyBook = beforeVocabularyBook();
        vocabularyBookRepository.save(vocabularyBook);
        Long wordId = vocabularyBook.getId();

        // when
        ResultActions resultActions = mvc.perform(post("/api/auth/my-vocabulary-book/" + wordId));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("나만의 단어장에 단어를 추가했습니다."));
    }

    @DisplayName("나만의 단어장에 단어 추가 실패")
    @Test
    void myVocabularyBookInsertFail() throws Exception {
        // given
        Authentication authentication = beforeUser();
        User user = (User) authentication.getPrincipal();
        userRepository.save(user);
        Long wordId = 1L;

        // when
        ResultActions resultActions = mvc.perform(post("/api/auth/my-vocabulary-book/" + wordId));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("400"))
                .andExpect(jsonPath("message").value("존재하지 않는 단어입니다."));
    }

    @DisplayName("나만의 단어장에 단어 삭제 성공")
    @Test
    void myVocabularyBookDelete() throws Exception {
        // given
        Authentication authentication = beforeUser();
        User user = (User) authentication.getPrincipal();
        userRepository.save(user);
        VocabularyBook vocabularyBook = beforeVocabularyBook();
        vocabularyBookRepository.save(vocabularyBook);
        MyVocabularyBook myVocabularyBook = new MyVocabularyBook(1L, user, vocabularyBook);
        myVocabularyBookRepository.save(myVocabularyBook);
        Long wordId = vocabularyBook.getId();

        // when
        ResultActions resultActions = mvc.perform(delete("/api/auth/my-vocabulary-book/" + wordId));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("나만의 단어장에 단어를 삭제했습니다."));
    }

    @DisplayName("나만의 단어장에 단어 삭제 실패")
    @Test
    void myVocabularyBookDeleteFail() throws Exception {
        // given
        Authentication authentication = beforeUser();
        User user = (User) authentication.getPrincipal();
        userRepository.save(user);
        VocabularyBook vocabularyBook = beforeVocabularyBook();
        vocabularyBookRepository.save(vocabularyBook);
        Long wordId = vocabularyBook.getId();

        // when
        ResultActions resultActions = mvc.perform(delete("/api/auth/my-vocabulary-book/" + wordId));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("400"))
                .andExpect(jsonPath("message").value("존재하지 않는 나만의 단어입니다."));
    }

    @DisplayName("나만의 단어장에 단어 조회 성공")
    @Test
    void myVocabularyBookList() throws Exception {
        // given
        Authentication authentication = beforeUser();
        User user = (User) authentication.getPrincipal();
        userRepository.save(user);
        VocabularyBook vocabularyBook = beforeVocabularyBook();
        vocabularyBookRepository.save(vocabularyBook);
        MyVocabularyBook myVocabularyBook = new MyVocabularyBook(1L, user, vocabularyBook);
        myVocabularyBookRepository.save(myVocabularyBook);

        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/my-vocabulary-book"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("나만의 단어장에 단어를 조회했습니다."));
    }
}