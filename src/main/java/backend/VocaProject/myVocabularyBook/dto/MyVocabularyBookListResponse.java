package backend.VocaProject.myVocabularyBook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyVocabularyBookListResponse {

    private Long wordId;

    private String word;

    private String meaning;

    private String categoryName;
}
