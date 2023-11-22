package backend.VocaProject.vocabularyBook.dto;

import backend.VocaProject.domain.VocabularyBookCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyBookListResponse {

    private List<VocabularyBookResponse> responseList;

    private int firstDay;

    private int lastDay;

    private long totalCount;

    private String categoryName;
}
