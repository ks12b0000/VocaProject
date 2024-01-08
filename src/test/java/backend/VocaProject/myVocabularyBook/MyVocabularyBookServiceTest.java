package backend.VocaProject.myVocabularyBook;

import backend.VocaProject.domain.MyVocabularyBook;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBook;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.myVocabularyBook.dto.MyVocabularyBookListResponse;
import backend.VocaProject.user.dto.JoinRequest;
import backend.VocaProject.vocabularyBook.VocabularyBookRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
class MyVocabularyBookServiceTest {

    @InjectMocks
    private MyVocabularyBookServiceImpl myVocabularyBookService;

    @Mock
    private VocabularyBookRepository vocabularyBookRepository;

    @Mock
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
    void myVocabularyBookInsert() {
        // given
        Authentication user = beforeUser();
        VocabularyBook vocabularyBook = beforeVocabularyBook();
        Long wordId = vocabularyBook.getId();

        // stub
        when(vocabularyBookRepository.findById(wordId)).thenReturn(Optional.ofNullable(vocabularyBook));

        // when
        myVocabularyBookService.myVocabularyBookInsert(user, wordId);
        // then

    }

    @DisplayName("나만의 단어장에 단어 추가 실패")
    @Test
    void myVocabularyBookInsertFail() {
        // given
        Authentication user = beforeUser();
        VocabularyBook vocabularyBook = beforeVocabularyBook();
        Long wordId = vocabularyBook.getId();

        // stub
        when(vocabularyBookRepository.findById(wordId)).thenReturn(Optional.ofNullable(vocabularyBook));
        when(myVocabularyBookRepository.existsByUserAndVocabularyBook(any(), any())).thenReturn(true);

        // when
        // then
        assertThatThrownBy(() -> myVocabularyBookService.myVocabularyBookInsert(user, wordId)).hasMessage("이미 나만의 단어장에 추가한 단어입니다.");
    }

    @DisplayName("나만의 단어장에 단어 삭제 성공")
    @Test
    void myVocabularyBookDelete() {
        // given
        Authentication user = beforeUser();
        VocabularyBook vocabularyBook = beforeVocabularyBook();
        Long wordId = vocabularyBook.getId();
        User authUser = (User) user.getPrincipal();
        MyVocabularyBook myVocabularyBook = new MyVocabularyBook(authUser, vocabularyBook);

        // stub
        when(vocabularyBookRepository.findById(wordId)).thenReturn(Optional.ofNullable(vocabularyBook));
        when(myVocabularyBookRepository.findByUserAndVocabularyBook(any(), any())).thenReturn(Optional.of(myVocabularyBook));

        // when
        myVocabularyBookService.myVocabularyBookDelete(user, wordId);
        // then
    }

    @DisplayName("나만의 단어장에 단어 삭제 실패")
    @Test
    void myVocabularyBookDeleteFail() {
        // given
        Authentication user = beforeUser();
        VocabularyBook vocabularyBook = beforeVocabularyBook();
        Long wordId = vocabularyBook.getId();

        // stub
        when(vocabularyBookRepository.findById(wordId)).thenReturn(Optional.ofNullable(vocabularyBook));

        // when
        // then
        assertThatThrownBy(() -> myVocabularyBookService.myVocabularyBookDelete(user, wordId)).hasMessage("존재하지 않는 나만의 단어입니다.");
    }

    @DisplayName("나만의 단어장에 단어 조회 성공")
    @Test
    void myVocabularyBookList() {
        // given
        Authentication user = beforeUser();
        User authUser = (User) user.getPrincipal();
        List<MyVocabularyBookListResponse> list = new ArrayList<>();
        list.add(new MyVocabularyBookListResponse(1L, "word", "meaning", "category"));

        // stub
        when(myVocabularyBookRepository.findByMyVocabularyBookList(authUser)).thenReturn(list);

        // when
        List<MyVocabularyBookListResponse> responses = myVocabularyBookService.myVocabularyBookList(user);

        // then
        assertAll(
                () -> assertThat(responses.get(0).getCategoryName()).isEqualTo("category"),
                () -> assertThat(responses.get(0).getWord()).isEqualTo("word"),
                () -> assertThat(responses.get(0).getMeaning()).isEqualTo("meaning")
        );
    }
}