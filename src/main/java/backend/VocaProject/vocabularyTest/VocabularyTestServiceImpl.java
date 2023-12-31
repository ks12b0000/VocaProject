package backend.VocaProject.vocabularyTest;

import backend.VocaProject.domain.User;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.domain.VocabularyTest;
import backend.VocaProject.response.BaseException;
import backend.VocaProject.vocabularyBook.VocabularyBookRepository;
import backend.VocaProject.vocabularyBookCategory.VocabularyBookCategoryRepository;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestListResponse;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestResponse;
import backend.VocaProject.vocabularyTest.dto.VocabularyTestResultRequest;
import backend.VocaProject.vocabularyTestSetting.VocabularyTestSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static backend.VocaProject.response.BaseExceptionStatus.NON_EXISTENT_VOCABULARY_BOOK;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class VocabularyTestServiceImpl implements VocabularyTestService {

    private final VocabularyTestRepository vocabularyTestRepository;

    private final VocabularyBookCategoryRepository categoryRepository;

    private final VocabularyBookRepository vocabularyBookRepository;

    private final VocabularyTestSettingRepository settingRepository;

    /**
     * 단어 테스트 조회
     * user, category, firstDay, lastDay로 단어 테스트를 볼 카테고리, 기간으로 단어장을 조회하고
     * 단어 테스트 리스트, 기간, 총 테스트 단어 갯수, 카테고리 이름, 목표 점수, 테스트 본 횟수를 반환한다.
     * @param auth
     * @param categoryId
     * @param firstDay
     * @param lastDay
     * @return
     */
    @Override
    public VocabularyTestListResponse vocabularyTest(Authentication auth, Long categoryId, int firstDay, int lastDay) {
        VocabularyBookCategory category = categoryRepository.findById(categoryId).orElseThrow(() -> new BaseException(NON_EXISTENT_VOCABULARY_BOOK));
        List<Tuple> tuples = vocabularyBookRepository.findByVocabularyTestList(category.getId(), firstDay, lastDay);
        User user = (User) auth.getPrincipal();
        Integer testTargetScore = settingRepository.findByUserVocabularyTestTargetScore(user, category, firstDay, lastDay);
        Integer testCount = vocabularyTestRepository.findByTestCount(user, category, firstDay, lastDay);

        VocabularyTestListResponse response = new VocabularyTestListResponse(mapTuplesToResponses(tuples), firstDay, lastDay, tuples.size(), category.getName(), testTargetScore, testCount);

        return response;
    }

    private List<VocabularyTestResponse> mapTuplesToResponses(List<Tuple> tuples) {
        return tuples.stream()
                .map(tuple -> VocabularyTestResponse.builder()
                        .wordId(tuple.get(0, BigInteger.class).longValue())
                        .word(tuple.get(1, String.class))
                        .originalMeaning(tuple.get(2, String.class))
                        .wrongMeaning1(tuple.get(3, String.class))
                        .wrongMeaning2(tuple.get(4, String.class))
                        .wrongMeaning3(tuple.get(5, String.class))
                        .wrongMeaning4(tuple.get(6, String.class))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 단어 테스트 결과 저장
     * 유저, 카테고리, 테스트 범위로 테스트 결과가 저장 되어있는지 확인 후
     * 만약 이미 저장 된 테스트 결과라면 테스트 횟수, 결과, 성적, 틀린 단어를 업데이트 하고
     * 저장 되어있지 않다면 유저, 카테고리, 테스트 횟수, 결과, 성적, 범위, 틀린 단어로 테스트 결과를 저장한다.
     * @param auth
     * @param request
     */
    @Override
    @Transactional
    public void vocabularyTestResultSave(Authentication auth, VocabularyTestResultRequest request) {
        User user = (User) auth.getPrincipal();
        VocabularyBookCategory category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new BaseException(NON_EXISTENT_VOCABULARY_BOOK));
        VocabularyTest vocabularyTest = vocabularyTestRepository.findByUserAndVocabularyBookCategoryAndFirstDayAndLastDay(user, category, request.getFirstDay(), request.getLastDay());

        if (vocabularyTest != null) {
            vocabularyTest.testUpdate(vocabularyTest.getTestCount() + 1, request.getResult(), request.getRecord(), request.getWrongWord());
        } else {
            vocabularyTest = new VocabularyTest(user, category, 1, request.getResult(), request.getRecord(), request.getFirstDay(), request.getLastDay(), request.getWrongWord());
            vocabularyTestRepository.save(vocabularyTest);
        }
    }
}
