package backend.VocaProject.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@AllArgsConstructor
public class VocabularyTest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "testId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private VocabularyBookCategory vocabularyBookCategory;

    @Column(nullable = false)
    private int testCount;

    @Column(length = 50, nullable = false)
    private String result;

    @Column(length = 50, nullable = false)
    private String record;

    @Column(nullable = false)
    private int firstDay;

    @Column(nullable = false)
    private int lastDay;

    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> wrongWords;

    public VocabularyTest(User user, VocabularyBookCategory vocabularyBookCategory, int testCount, String result, String record, int firstDay, int lastDay, List<String> wrongWords) {
        this.user = user;
        this.vocabularyBookCategory = vocabularyBookCategory;
        this.testCount = testCount;
        this.result = result;
        this.record = record;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.wrongWords = wrongWords;
    }

    public void testUpdate(int testCount, String result, String record, List<String> wrongWords) {
        this.testCount = testCount;
        this.result = result;
        this.record = record;
        this.wrongWords = wrongWords;
    }
}
