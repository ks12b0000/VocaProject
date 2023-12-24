package backend.VocaProject.vocabularyTest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyTestListResponse {

    private List<VocabularyTestResponse> responseList;

    private int firstDay;

    private int lastDay;

    private long totalCount;

    private String categoryName;

    private Integer targetScore;

    private Integer testCount;
}
