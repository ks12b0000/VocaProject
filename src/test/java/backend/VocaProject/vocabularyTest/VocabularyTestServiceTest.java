package backend.VocaProject.vocabularyTest;

import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBook;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.vocabularyBook.VocabularyBookRepository;
import backend.VocaProject.vocabularyBook.dto.WrongWordsResponse;
import backend.VocaProject.vocabularyBookCategory.VocabularyBookCategoryRepository;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestListResponse;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestResultRequest;
import backend.VocaProject.vocabularyTest.dto.WrongWordsListResponse;
import backend.VocaProject.vocabularyTestSetting.VocabularyTestSettingRepository;
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

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
class VocabularyTestServiceTest {

    @InjectMocks
    private VocabularyTestServiceImpl vocabularyTestService;

    @Mock
    private VocabularyBookCategoryRepository categoryRepository;

    @Mock
    private VocabularyBookRepository vocabularyBookRepository;

    @Mock
    private VocabularyTestSettingRepository settingRepository;

    @Mock
    private VocabularyTestRepository testRepository;

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

    @DisplayName("단어 테스트 조회 성공")
    @Test
    void vocabularyTest() {
        // given
        Authentication user = beforeUser();
        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");

        // stub
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        // when
        VocabularyTestListResponse response = vocabularyTestService.vocabularyTest(user, 1L, 1, 10);

        // then
        assertAll(
                () -> assertThat(response.getTestCount()).isEqualTo(0),
                () -> assertThat(response.getCategoryName()).isEqualTo(category.getName()),
                () -> assertThat(response.getFirstDay()).isEqualTo(1),
                () -> assertThat(response.getLastDay()).isEqualTo(10),
                () -> assertThat(response.getTargetScore()).isEqualTo(0)
        );
    }

    @DisplayName("단어 테스트 조회 실패")
    @Test
    void vocabularyTestFail() {
        // given
        Authentication user = beforeUser();

        // when
        // then
        assertThatThrownBy(() -> vocabularyTestService.vocabularyTest(user, 1L, 1, 10)).hasMessage("존재하지 않는 단어장입니다.");
    }

    @DisplayName("단어 테스트 결과 저장 성공")
    @Test
    void vocabularyTestResultSave() {
        // given
        Authentication user = beforeUser();
        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");
        VocabularyTestResultRequest request = new VocabularyTestResultRequest(category.getId(), 1, 10, "result", "record", null);

        // stub
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        // when
        vocabularyTestService.vocabularyTestResultSave(user, request);

        // then
    }

    @DisplayName("단어 테스트 결과 저장 실패")
    @Test
    void vocabularyTestResultSaveFail() {
        // given
        Authentication user = beforeUser();
        VocabularyBookCategory category = new VocabularyBookCategory(1L, "중등 초급");
        VocabularyTestResultRequest request = new VocabularyTestResultRequest(category.getId(), 1, 10, "result", "record", null);

        // when
        // then
        assertThatThrownBy(() -> vocabularyTestService.vocabularyTestResultSave(user, request)).hasMessage("존재하지 않는 단어장입니다.");
    }

    @DisplayName("단어 테스트 틀린 단어 조회 성공")
    @Test
    void vocabularyTestWrongWords() {
        // given
        Authentication user = beforeUser();
        List<WrongWordsResponse> list = new ArrayList<>();
        list.add(new WrongWordsResponse(1L, "hi", "안녕", 1L, 1));

        // stub
        when(vocabularyBookRepository.findByWordIn(anyList())).thenReturn(list);

        // when
        WrongWordsListResponse response = vocabularyTestService.vocabularyTestWrongWords(user);

        // then
        assertAll(
                () -> assertThat(response.getResponseList().get(0).getWord()).isEqualTo(list.get(0).getWord()),
                () -> assertThat(response.getResponseList().get(0).getMeaning()).isEqualTo(list.get(0).getMeaning()),
                () -> assertThat(response.getResponseList().get(0).getWordId()).isEqualTo(list.get(0).getWordId()),
                () -> assertThat(response.getResponseList().get(0).getCategoryId()).isEqualTo(list.get(0).getCategoryId()),
                () -> assertThat(response.getResponseList().get(0).getDay()).isEqualTo(list.get(0).getDay()),
                () -> assertThat(response.getTotalCount()).isEqualTo(list.size())
        );
    }

}