package backend.VocaProject.vocabularyTest.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularyTestResponse {

    private long wordId;

    private String word;

    private String originalMeaning;

    private String wrongMeaning1;

    private String wrongMeaning2;

    private String wrongMeaning3;

    private String wrongMeaning4;

}
