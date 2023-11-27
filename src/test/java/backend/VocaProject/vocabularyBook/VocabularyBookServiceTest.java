package backend.VocaProject.vocabularyBook;

import backend.VocaProject.vocabularyLearning.VocabularyLearningRepository;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.vocabularyBook.dto.VocabularyBookListResponse;
import backend.VocaProject.vocabularyBook.dto.VocabularyLearningRequest;
import backend.VocaProject.vocabularyBookCategory.VocabularyBookCategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
class VocabularyBookServiceTest {

    @InjectMocks
    private VocabularyBookServiceImpl vocabularyBookService;

    @Mock
    private VocabularyBookRepository vocabularyBookRepository;

    @Mock
    private VocabularyBookCategoryRepository categoryRepository;

    @Mock
    private VocabularyLearningRepository vocabularyLearningRepository;

    @DisplayName("단어장 조회 성공")
    @Test
    void vocabularyBooks() {
        // given
        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");
        Long categoryId = 1L;
        int firstDay = 1;
        int lastDay = 20;

        // stub
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // when
        VocabularyBookListResponse response = vocabularyBookService.vocabularyBooks(categoryId, firstDay, lastDay);

        // then
        Assertions.assertThat(response).isNotNull();

    }

    @DisplayName("단어장 조회 실패")
    @Test
    void vocabularyBooksFail() {
        // given
        Long categoryId = 1L;
        int firstDay = 1;
        int lastDay = 20;

        // stub
        // when
        // then
        assertThatThrownBy(() -> vocabularyBookService.vocabularyBooks(categoryId, firstDay, lastDay)).hasMessage("존재하지 않는 단어장입니다.");

    }

    @DisplayName("단어장 학습 종료시 학습 저장 성공")
    @Test
    void vocabularyBookEndByLearningSave() {
        // given
        User user = new User("홍길동", "login1234", "password12");
        user.setClassName("중등 초급");
        user.setRole("ROLE_USER");
        Authentication authUser = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authUser);
        VocabularyLearningRequest request = new VocabularyLearningRequest(1L, 30L, 1, 2);
        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");

        // stub
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(vocabularyLearningRepository.findByUserAndVocabularyBookCategory(any(), any())).thenReturn(null);

        // when
        vocabularyBookService.vocabularyBookEndByLearningSave(authUser, request);

    }

    @DisplayName("단어장 학습 종료시 학습 저장 실패")
    @Test
    void vocabularyBookEndByLearningFail() {
        // given
        User user = new User("홍길동", "login1234", "password12");
        user.setClassName("중등 초급");
        user.setRole("ROLE_USER");
        Authentication authUser = new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authUser);
        VocabularyLearningRequest request = new VocabularyLearningRequest(1L, 30L, 1, 2);

        // stub
        // when
        // then
        assertThatThrownBy(() -> vocabularyBookService.vocabularyBookEndByLearningSave(authUser, request)).hasMessage("존재하지 않는 단어장입니다.");

    }
}