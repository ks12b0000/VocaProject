package backend.VocaProject.vocabularyBook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WrongWordsResponse {

    private long wordId;

    private String word;

    private String meaning;

    private long categoryId;

    private int day;
}
