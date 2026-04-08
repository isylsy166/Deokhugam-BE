package deokhugam.global.exception;

import lombok.Getter;

@Getter
public class DeokhugamException extends RuntimeException {
    private final ErrorCode errorCode;

    public DeokhugamException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}