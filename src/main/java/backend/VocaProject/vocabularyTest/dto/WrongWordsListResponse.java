package backend.VocaProject.vocabularyTest.dto;

import backend.VocaProject.domain.VocabularyBook;
import backend.VocaProject.vocabularyBook.dto.WrongWordsResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WrongWordsListResponse {

    private List<WrongWordsResponse> responseList;

    private long totalCount;

}
