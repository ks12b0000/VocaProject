package backend.VocaProject.vocabularyBook;

import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.vocabularyBook.dto.VocabularyBookListResponse;
import backend.VocaProject.vocabularyBookCategory.VocabularyBookCategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        Assertions.assertThatThrownBy(() -> vocabularyBookService.vocabularyBooks(categoryId, firstDay, lastDay)).hasMessage("존재하지 않는 단어장입니다.");

    }
}