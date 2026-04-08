package deokhugam.domain.user.exception;

import deokhugam.global.exception.DomainErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements DomainErrorCode {

    USER_NOT_EXIST("U001", "존재하지 않는 사용자 입니다.", HttpStatus.NOT_FOUND),
    PASSWORD_NOT_CORRECT("U002", "아이디 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    DUPLICATE_EMAIL("U003","이미 가입된 사용자입니다.", HttpStatus.CONFLICT),
    DUPLICATE_NICKNAME("U004", "이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT),
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
