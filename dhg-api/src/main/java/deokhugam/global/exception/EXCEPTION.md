# Exception Guide

## 목적

Deokhugam API의 예외 처리는 다음 두 가지를 목표로 합니다.

- 예외를 발생시키는 위치와 책임을 분리한다.
- 클라이언트에게 일관된 에러 응답을 제공한다.

## 예외 계층

### `DeokhugamException`

전역 공통 예외를 위한 베이스 예외입니다.

- 인증/인가
- 잘못된 요청
- 공통 인프라 또는 시스템성 예외
- 특정 도메인에 한정되지 않는 예외

`ErrorCode`를 통해 메시지와 상태 코드를 관리합니다.

### `DomainException`

도메인 비즈니스 예외를 위한 베이스 예외입니다.

- 사용자, 책, 리뷰 등 특정 도메인의 규칙 위반
- 서비스 로직에서 예상 가능한 비즈니스 실패
- 도메인별 에러 코드가 필요한 경우

`DomainErrorCode` 구현체를 통해 메시지와 상태 코드를 관리합니다.

예시:

- 존재하지 않는 사용자
- 비밀번호 불일치
- 중복 회원 가입

## ErrorCode 규칙

### `ErrorCode`

공통 예외에서 사용하는 전역 에러 코드입니다.

- `INTERNAL_SERVER_ERROR`
- `INVALID_REQUEST`
- `INVALID_INPUT_VALUE`
- `FORBIDDEN`

공통 정책이 필요한 예외는 이 enum에서 관리합니다.

### `DomainErrorCode`

도메인별 에러 코드 규약입니다.

각 도메인은 자신의 `*ErrorCode` enum이 `DomainErrorCode`를 구현하도록 합니다.

예시:

- `UserErrorCode`
- `BookErrorCode`
- `ReviewErrorCode`

권장 규칙:

- 에러 코드는 도메인 prefix를 포함한다. 예: `U001`
- 메시지는 사용자 응답 기준으로 이해 가능하게 작성한다.
- HTTP 상태 코드는 예외 의미와 일치해야 한다.

## 예외 작성 규칙

### 공통 규칙

- 예외 메시지는 임의 문자열보다 에러 코드에서 관리한다.
- 서비스 계층에서는 의미 없는 `RuntimeException` 대신 명시적인 커스텀 예외를 사용한다.
- 동일한 실패 상황에는 항상 동일한 에러 코드를 사용한다.

### 도메인 예외 규칙

- 도메인 예외는 `DomainException`을 상속한다.
- 도메인 예외는 해당 도메인의 `*ErrorCode`만 받는다.
- 비즈니스 규칙 위반은 가능한 한 도메인 예외로 표현한다.

예시:

```java
throw new UserException(UserErrorCode.USER_NOT_EXIST);
```

### 전역 예외 규칙

- 전역 공통 정책이 필요한 예외는 `DeokhugamException`을 사용한다.
- 특정 도메인에 속하지 않는 예외만 전역 예외로 둔다.

## 예외 처리 흐름

1. 서비스 또는 도메인 로직에서 예외를 던진다.
2. `RestControllerAdvice`에서 예외를 가로챈다.
3. `ProblemDetail`로 변환해 응답한다.
4. 클라이언트는 일관된 형식의 에러 응답을 받는다.

## 응답 원칙

에러 응답은 가능한 한 같은 구조를 유지해야 합니다.

권장 필드:

- `title`: 예외 이름 또는 에러 식별자
- `detail`: 사용자에게 보여줄 메시지
- `status`: HTTP 상태 코드
- `code`: 내부 에러 코드

예시:

```json
{
  "title": "USER_NOT_EXIST",
  "detail": "존재하지 않는 사용자 입니다.",
  "status": 404,
  "code": "U001"
}
```

## 새 도메인 예외 추가 방법

1. 도메인 패키지에 `*ErrorCode` enum을 만든다.
2. `DomainErrorCode`를 구현한다.
3. 도메인 전용 `*Exception` 클래스를 만든다.
4. 서비스 계층에서 해당 예외를 사용한다.

예시:

```java
public enum UserErrorCode implements DomainErrorCode {
    USER_NOT_EXIST("U001", "존재하지 않는 사용자 입니다.", HttpStatus.NOT_FOUND);
}
```

```java
public class UserException extends DomainException {
    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
```

## 현재 구조에서 주의할 점

- `DeokhugamException`과 `DomainException`은 의도적으로 분리되어 있다.
- 분리된 만큼 어떤 예외를 어느 계층에 둘지 기준이 명확해야 한다.
- 응답 포맷이 핸들러마다 달라지지 않도록 주의해야 한다.
- 패키지 import가 섞이면 예외 처리 체계가 쉽게 무너질 수 있다.

## 정리

- 공통 예외는 `DeokhugamException`
- 도메인 예외는 `DomainException`
- 메시지와 상태 코드는 코드 객체에서 관리
- 예외 응답은 `ProblemDetail` 기반으로 일관되게 유지

이 문서는 예외 구조를 설명하는 기준 문서이며, 구현이 변경되면 함께 갱신합니다.
