package backend.VocaProject.vocabularyBook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LastLearningAndTestRangeResponse {

    private int learningFirstDay;

    private int learningLastDay;

    private int testFirstDay;

    private int testLastDay;
}
