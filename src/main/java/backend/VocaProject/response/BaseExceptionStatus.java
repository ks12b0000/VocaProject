package backend.VocaProject.response;

import lombok.Getter;

@Getter
public enum BaseExceptionStatus {

    SERVER_INTERNAL_ERROR(2002, "서버 내부적인 에러"),

    DUPLICATE_LOGIN_ID(3001, "중복된 아이디가 있습니다."),
    LOGIN_USER_NOT_EXIST(3002, "아이디 또는 비밀번호가 일치하지 않습니다."),
    WITHOUT_ACCESS_USER(3003, "권한이 없습니다. 관리자에게 문의하세요."),
    NON_EXISTENT_USER(3004, "존재하지 않는 유저입니다."),
    NON_EXISTENT_BOOK(4001, "존재하지 않는 단어장입니다."),

    UNAUTHORIZED_USER_ACCESS(5001, "인증되지 않은 유저의 접근입니다."),

    JWT_TOKEN_EXPIRE(5002, "JWT 토큰 만료되었습니다."),
    JWT_TOKEN_INVALID(5003,"잘못된 JWT 토큰입니다.");


    private final int code;
    private final String message;

    private BaseExceptionStatus(int code, String msg){
        this.code = code;
        this.message = msg;
    }
}
