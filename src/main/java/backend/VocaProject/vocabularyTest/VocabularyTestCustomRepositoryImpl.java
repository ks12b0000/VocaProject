package backend.VocaProject.vocabularyTest;

import backend.VocaProject.admin.dto.VocabularyTestResultListResponse;
import backend.VocaProject.domain.QVocabularyLearning;
import backend.VocaProject.domain.QVocabularyTest;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static backend.VocaProject.domain.QVocabularyTest.vocabularyTest;

@Repository
public class VocabularyTestCustomRepositoryImpl implements VocabularyTestCustomRepository {

    private final JPAQueryFactory queryFactory;

    public VocabularyTestCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public List<VocabularyTestResultListResponse> findByTestResultList(int size, LocalDateTime lastModifiedAt, String className) {
        QVocabularyTest vocabularyTest = QVocabularyTest.vocabularyTest;
        QVocabularyLearning vocabularyLearning = QVocabularyLearning.vocabularyLearning;

        // 1) 커버링 인덱스로 대상 조회
        List<Long> ids = queryFactory
                .select(vocabularyTest.id)
                .from(vocabularyTest)
                .where(eqClassName(className), ltModifiedAt(lastModifiedAt))
                .orderBy(vocabularyTest.modifiedAt.desc())
                .limit(size)
                .fetch();

        // 1-1) 대상이 없을 경우 추가 쿼리 수행 할 필요 없이 바로 반환
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        return queryFactory
                .select(Projections.constructor(VocabularyTestResultListResponse.class,
                        vocabularyTest.user.username,
                        vocabularyTest.testCount,
                        vocabularyTest.result,
                        vocabularyTest.record,
                        vocabularyTest.vocabularyBookCategory.name,
                        vocabularyTest.firstDay,
                        vocabularyTest.lastDay,
                        vocabularyTest.modifiedAt,
                                JPAExpressions.select(vocabularyLearning.learningTime)
                                        .from(vocabularyLearning)
                                        .where(
                                                vocabularyLearning.user.eq(vocabularyTest.user),
                                                vocabularyLearning.vocabularyBookCategory.eq(vocabularyTest.vocabularyBookCategory),
                                                vocabularyLearning.firstDay.eq(vocabularyTest.firstDay),
                                                vocabularyLearning.lastDay.eq(vocabularyTest.lastDay)
                                        )

                ))
                .from(vocabularyTest)
                .where(vocabularyTest.id.in(ids))
                .orderBy(vocabularyTest.modifiedAt.desc())
                .fetch();
    }

    private BooleanExpression eqClassName(String className) {
        if (className == null) {
            return null;
        }
        return vocabularyTest.user.className.eq(className);
    }

    private BooleanExpression ltModifiedAt(LocalDateTime modifiedAt) {
        if (modifiedAt == null) {
            return null;
        }
        return vocabularyTest.modifiedAt.lt(modifiedAt);
    }
}
