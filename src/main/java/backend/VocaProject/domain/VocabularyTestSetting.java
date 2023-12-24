package backend.VocaProject.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class VocabularyTestSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settingId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private VocabularyBookCategory vocabularyBookCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private int targetScore;

    @Column(nullable = false)
    private int firstDay;

    @Column(nullable = false)
    private int lastDay;

    public VocabularyTestSetting(VocabularyBookCategory vocabularyBookCategory, User user, int targetScore, int firstDay, int lastDay) {
        this.vocabularyBookCategory = vocabularyBookCategory;
        this.user = user;
        this.targetScore = targetScore;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }

    public void updateTargetScore(int targetScore) {
        this.targetScore = targetScore;
    }
}
