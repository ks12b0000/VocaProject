package backend.VocaProject.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class MyVocabularyBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "number")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wordId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private VocabularyBook vocabularyBook;

    public MyVocabularyBook(User user, VocabularyBook vocabularyBook) {
        this.user = user;
        this.vocabularyBook = vocabularyBook;
    }
}
