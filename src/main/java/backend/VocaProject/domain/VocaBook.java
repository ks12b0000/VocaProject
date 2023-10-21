package backend.VocaProject.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Optional;

@NoArgsConstructor
@Entity
@Getter
public class VocaBook {

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
    private VocaBookCategory vocaBookCategory;

    public VocaBook(String word, String meaning, VocaBookCategory vocaBookCategory) {
        this.word = word;
        this.meaning = meaning;
        this.vocaBookCategory = vocaBookCategory;
    }
}
