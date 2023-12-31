package backend.VocaProject.vocabularyBook;

import backend.VocaProject.domain.VocabularyTest;
import backend.VocaProject.vocabularyBook.dto.LastLearningAndTestRangeResponse;
import backend.VocaProject.vocabularyLearning.VocabularyLearningRepository;
import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.domain.VocabularyLearning;
import backend.VocaProject.response.BaseException;
import backend.VocaProject.vocabularyBook.dto.VocabularyBookListResponse;
import backend.VocaProject.vocabularyBook.dto.VocabularyBookResponse;
import backend.VocaProject.vocabularyBook.dto.VocabularyLearningRequest;
import backend.VocaProject.vocabularyBookCategory.VocabularyBookCategoryRepository;
import backend.VocaProject.vocabularyTest.VocabularyTestRepository;
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

    private final VocabularyLearningRepository vocabularyLearningRepository;

    private final VocabularyTestRepository testRepository;

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

    /**
     * 단어장 학습 종료시 학습 저장
     * user, categoryId, learningTime, firstDay, lastDay로 단어장 학습 내용을 저장한다.
     * 만약 이미 학습이 저장된 카테고리라면, 학습시간을 플러스 하고, firstDay, lastDay를 업데이트 한다.
     * 처음 학습을 저장한다면 학습 내용을 저장한다.
     * @param auth
     * @param request
     */
    @Override
    @Transactional
    public void vocabularyBookEndByLearningSave(Authentication auth, VocabularyLearningRequest request) {
        User user = (User) auth.getPrincipal();
        VocabularyBookCategory category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new BaseException(NON_EXISTENT_VOCABULARY_BOOK));

        VocabularyLearning vocabularyLearning = vocabularyLearningRepository.findByUserAndVocabularyBookCategoryAndFirstDayAndLastDay(user, category, request.getFirstDay(), request.getLastDay());

        if (vocabularyLearning != null) {
            vocabularyLearning.updateVocabularyLearning(vocabularyLearning.getLearningTime() + request.getLearningTime(), request.getFirstDay(), request.getLastDay());
        } else {
            vocabularyLearning = new VocabularyLearning(category, user, request.getLearningTime(), request.getFirstDay(), request.getLastDay());
            vocabularyLearningRepository.save(vocabularyLearning);
        }
    }

    /**
     * 단어장 카테고리별 마지막 학습, 테스트 범위 조회
     * 유저, 카테고리로 마지막 학습 범위, 마지막 테스트 범위를 조회한다.
     * @param auth
     * @param categoryId
     * @return
     */
    @Override
    public LastLearningAndTestRangeResponse findByLastLearningAndTestRange(Authentication auth, Long categoryId) {
        User user = (User) auth.getPrincipal();
        VocabularyBookCategory category = categoryRepository.findById(categoryId).orElseThrow(() -> new BaseException(NON_EXISTENT_VOCABULARY_BOOK));
        VocabularyLearning vocabularyLearning = vocabularyLearningRepository.findTop1ByUserAndVocabularyBookCategoryOrderByModifiedAtDesc(user, category);
        VocabularyTest vocabularyTest = testRepository.findTop1ByUserAndVocabularyBookCategoryOrderByModifiedAtDesc(user, category);

        int learningFirstDay = (vocabularyLearning != null) ? vocabularyLearning.getFirstDay() : 0;
        int learningLastDay = (vocabularyLearning != null) ? vocabularyLearning.getLastDay() : 0;
        int testFirstDay = (vocabularyTest != null) ? vocabularyTest.getFirstDay() : 0;
        int testLastDay = (vocabularyTest != null) ? vocabularyTest.getLastDay() : 0;

        LastLearningAndTestRangeResponse response = new LastLearningAndTestRangeResponse(learningFirstDay, learningLastDay, testFirstDay, testLastDay);

        return response;
    }
}
