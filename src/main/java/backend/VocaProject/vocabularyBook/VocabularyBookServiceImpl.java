package backend.VocaProject.vocabularyBook;

import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.response.BaseException;
import backend.VocaProject.vocabularyBook.dto.VocabularyBookListResponse;
import backend.VocaProject.vocabularyBook.dto.VocabularyBookResponse;
import backend.VocaProject.vocabularyBookCategory.VocabularyBookCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static backend.VocaProject.response.BaseExceptionStatus.NON_EXISTENT_VOCABULARY_BOOK;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class VocabularyBookServiceImpl implements VocabularyBookService{

    private final VocabularyBookRepository vocabularyBookRepository;

    private final VocabularyBookCategoryRepository categoryRepository;

    /**
     * 단어장 조회
     * categoryId, firstDay, lastDay로 단어장 종류, 기간으로 단어장을 조회해서
     * 단어장 리스트, 기간, 총 단어 갯수, 카테고리 이름을 반환한다.
     * @param categoryId
     * @param firstDay
     * @param lastDay
     * @return
     */
    @Override
    public VocabularyBookListResponse vocabularyBooks(Long categoryId, int firstDay, int lastDay) {
        VocabularyBookCategory category = categoryRepository.findById(categoryId).orElseThrow(() -> new BaseException(NON_EXISTENT_VOCABULARY_BOOK));
        List<VocabularyBookResponse> list = vocabularyBookRepository.findByVocabularyList(category, firstDay, lastDay);

        VocabularyBookListResponse response = new VocabularyBookListResponse(list, firstDay, lastDay, list.size(), category.getName());

        return response;
    }
}
