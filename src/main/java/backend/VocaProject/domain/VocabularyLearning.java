package backend.VocaProject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Builder
@AllArgsConstructor
public class VocabularyLearning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learningId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private VocabularyBookCategory vocabularyBookCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column
    private Long learningTime;

    @Column
    private int firstDay;

    @Column
    private int lastDay;

    public VocabularyLearning(VocabularyBookCategory vocabularyBookCategory, User user, Long learningTime, int firstDay, int lastDay) {
        this.vocabularyBookCategory = vocabularyBookCategory;
        this.user = user;
        this.learningTime = learningTime;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }

    public void updateVocabularyLearning(Long learningTime, int firstDay, int lastDay) {
        this.learningTime = learningTime;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }
}
