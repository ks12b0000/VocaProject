package backend.VocaProject.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class VocabularyBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wordId")
    private Long id;

    @Column(length = 50, nullable = false)
    private String word;

    @Column(length = 50, nullable = false)
    private String meaning;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private VocabularyBookCategory vocabularyBookCategory;

    public VocabularyBook(String word, String meaning, VocabularyBookCategory vocabularyBookCategory) {
        this.word = word;
        this.meaning = meaning;
        this.vocabularyBookCategory = vocabularyBookCategory;
    }
}
