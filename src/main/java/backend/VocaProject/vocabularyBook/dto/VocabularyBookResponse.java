package backend.VocaProject.vocabularyBook.dto;

import backend.VocaProject.domain.VocabularyBookCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyBookResponse {

    private long wordId;

    private String word;

    private String meaning;

    private Long myVocabularyBook;

}
