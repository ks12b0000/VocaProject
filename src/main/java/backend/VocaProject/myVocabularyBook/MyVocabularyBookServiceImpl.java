package backend.VocaProject.myVocabularyBook;

import backend.VocaProject.domain.MyVocabularyBook;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBook;
import backend.VocaProject.myVocabularyBook.dto.MyVocabularyBookListResponse;
import backend.VocaProject.response.BaseException;
import backend.VocaProject.vocabularyBook.VocabularyBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static backend.VocaProject.response.BaseExceptionStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MyVocabularyBookServiceImpl implements MyVocabularyBookService {

    private final MyVocabularyBookRepository myVocabularyBookRepository;

    private final VocabularyBookRepository vocabularyBookRepository;

    /**
     * 나만의 단어장에 단어 추가
     * user, wordId로 나만의 단어장에 단어 추가
     * 만약 존재하지 않는 단어, 이미 나만의 단어장에 추가한 단어라면 예외 처리
     * @param auth
     * @param wordId
     */
    @Transactional
    @Override
    public void myVocabularyBookInsert(Authentication auth, Long wordId) {
        User user = (User) auth.getPrincipal();
        VocabularyBook vocabularyBook = vocabularyBookRepository.findById(wordId).orElseThrow(() -> new BaseException(NON_EXISTENT_WORD));

        if (myVocabularyBookRepository.existsByUserAndVocabularyBook(user, vocabularyBook)) {
            throw new BaseException(DUPLICATE_MY_VOCABULARY_BOOK);
        }

        MyVocabularyBook myVocabularyBook = MyVocabularyBook.builder().user(user).vocabularyBook(vocabularyBook).build();

        myVocabularyBookRepository.save(myVocabularyBook);
    }

    /**
     * 나만의 단어장에 단어 삭제
     * user, wordId로 나만의 단어장에 단어 삭제
     * 만약 존재하지 않는 단어, 존재하지 않는 나만의 단어장의 단어라면 예외 처리
     * @param auth
     * @param wordId
     */
    @Transactional
    @Override
    public void myVocabularyBookDelete(Authentication auth, Long wordId) {
        User user = (User) auth.getPrincipal();
        VocabularyBook vocabularyBook = vocabularyBookRepository.findById(wordId).orElseThrow(() -> new BaseException(NON_EXISTENT_WORD));
        MyVocabularyBook myVocabularyBook = myVocabularyBookRepository.findByUserAndVocabularyBook(user, vocabularyBook).orElseThrow(() -> new BaseException(NON_EXISTENT_MY_WORD));

        myVocabularyBookRepository.delete(myVocabularyBook);
    }

    /**
     * 나만의 단어장 조회
     * user로 나만의 단어장을 조회한다.
     * @param auth
     * @return
     */
    @Override
    public List<MyVocabularyBookListResponse> myVocabularyBookList(Authentication auth) {
        User user = (User) auth.getPrincipal();
        List<MyVocabularyBookListResponse> list = myVocabularyBookRepository.findByMyVocabularyBookList(user);

        return list;
    }
}
