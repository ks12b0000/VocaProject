package backend.VocaProject.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("modifiedAt")
public class VocabularyTestResultListResponse {

    private String userName;

    private int testCount;

    private String result;

    private String record;

    private String categoryName;

    private int firstDay;

    private int lastDay;

    @JsonFormat(pattern = "yy/MM/dd (E) HH:mm", locale = "en_KR")
    private LocalDateTime modifiedAt;

    private Long learningTime;

    public String getFormattedModifiedAt() {
        // DateTimeFormatter를 사용하여 LocalDateTime을 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd (E) HH:mm", Locale.KOREA);
        return modifiedAt.format(formatter);
    }
}
