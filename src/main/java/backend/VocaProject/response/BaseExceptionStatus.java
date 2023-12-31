package backend.VocaProject.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseExceptionStatus {

    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "중복된 아이디가 있습니다."),
    LOGIN_USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 일치하지 않습니다."),
    WITHOUT_ACCESS_USER(HttpStatus.FORBIDDEN, "권한이 없습니다. 관리자에게 문의하세요."),
    NON_EXISTENT_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."),
    NON_EXISTENT_VOCABULARY_BOOK(HttpStatus.BAD_REQUEST, "존재하지 않는 단어장입니다."),
    NON_EXISTENT_WORD(HttpStatus.BAD_REQUEST, "존재하지 않는 단어입니다."),
    DUPLICATE_MY_VOCABULARY_BOOK(HttpStatus.CONFLICT, "이미 나만의 단어장에 추가한 단어입니다."),
    NON_EXISTENT_MY_WORD(HttpStatus.BAD_REQUEST, "존재하지 않는 나만의 단어입니다."),
    DUPLICATE_VOCABULARY_TEST(HttpStatus.CONFLICT, "이미 설정한 단어 테스트입니다.");


    private final HttpStatus code;
    private final String message;
}
