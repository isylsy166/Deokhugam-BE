package deokhugam.domain.user.exception;

import deokhugam.global.exception.DomainException;
import lombok.Getter;

@Getter
public class UserException extends DomainException {

    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
