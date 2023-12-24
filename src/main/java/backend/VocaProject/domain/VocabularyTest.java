package backend.VocaProject.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

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

}
