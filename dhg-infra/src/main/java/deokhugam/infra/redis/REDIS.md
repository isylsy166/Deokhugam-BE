# Redis 구조 정리

`deokhugam.infra.redis` 패키지는 Redis를 사용하는 기능을 목적별로 나눠서 관리하기 위한 인프라 레이어다.
현재는 크게 `config`, `common`, `pubsub`로 구성되어 있고, 이후 `session`, `ranking` 같은 하위 패키지를 추가해 확장할 수 있다.

## 패키지 구조

```text
deokhugam.infra.redis
├── REDIS.md
├── config
│   ├── RedisConfig.java
│   └── RedisPubSubConfig.java
├── common
│   ├── RedisKeyGenerator.java
│   └── RedisNameSpace.java
├── session
│   ├── SessionRedisRepository.java
│   ├── SessionCacheService.java
│   └── dto
│       └── ContentSessionCache.java
├── ranking
│   ├── RankingRedisRepository.java
│   ├── RankingService.java
│   └── dto
│       └── RankingEntry.java
└── pubsub
    ├── RedisPublisher.java
    ├── RedisSubscriber.java
    ├── RedisMessageHandler.java
    └── dto
        └── NotificationMessage.java
```

## 패키지별 역할

### 1. `config`

Redis와 연결하고, Spring Bean을 등록하는 설정 패키지다.

- `RedisConfig`
  - `RedisTemplate<String, Object>`를 Bean으로 등록한다.
  - Redis에 저장할 때
    - key는 문자열(String)
    - value는 JSON
    형태로 직렬화되도록 설정한다.
  - Redis를 사용할 때 가장 기본이 되는 설정이다.

- `RedisPubSubConfig`
  - Redis pub/sub 기능을 위한 설정이다.
  - `notification` 채널을 `ChannelTopic`으로 등록한다.
  - `RedisMessageListenerContainer`를 생성하고 subscriber를 topic에 연결한다.
  - publisher가 메시지를 보내면 subscriber가 같은 채널에서 받을 수 있게 해준다.

즉, `config`는 "Redis를 어떻게 연결하고 어떤 Bean을 쓸지"를 정의하는 곳이다.

### 2. `common`

Redis 기능 여러 군데에서 공통으로 쓰는 요소를 모아두는 패키지다.

- `RedisNameSpace`
  - Redis key prefix와 TTL 정책을 enum으로 관리한다.
  - 예를 들어 `token:`, `user-summary:` 같은 prefix를 코드에 하드코딩하지 않고 한 곳에서 관리할 수 있다.
  - `createKey(identifier)`로 실제 Redis key를 생성한다.

즉, `common`은 "여러 Redis 기능에서 공통으로 쓰는 규칙"을 담는 곳이다.

### 3. `pubsub`

실시간 메시지 발행/구독을 담당하는 패키지다.

- `RedisPublisher`
  - `RedisTemplate.convertAndSend()`를 이용해 특정 채널에 메시지를 발행한다.
  - 현재는 `notification` 채널로 메시지를 보내도록 구성되어 있다.

- `RedisSubscriber`
  - Redis 채널을 구독하고 있다가 메시지가 오면 처리한다.
  - 현재는 수신한 메시지를 로그로 출력하는 최소 구현이다.
  - 이후 실제 알림 서비스, WebSocket 전송, SSE 전송 같은 로직으로 연결할 수 있다.

즉, `pubsub`는 "저장"이 아니라 "실시간 메시지 전달"을 담당한다.

## 현재 구조를 이렇게 나눈 이유

Redis는 한 가지 용도로만 쓰이지 않는다.
이 프로젝트에서도 앞으로 아래처럼 서로 성격이 다른 기능에 사용할 가능성이 있다.

- 세션 저장
- 실시간 랭킹
- 실시간 알림(pub/sub)

이 기능들은 모두 Redis를 쓰지만 사용하는 자료구조와 목적이 다르다.

- 세션 저장: key-value + TTL
- 랭킹: sorted set(ZSet)
- 실시간 알림: pub/sub

그래서 `RedisManager` 하나에 모든 기능을 몰아넣기보다, 목적별 패키지로 나누는 편이 더 읽기 쉽고 유지보수하기 좋다.

## 앞으로 확장할 수 있는 구조

현재 구조에서 아래 패키지를 추가하는 방향을 고려하고 있다.

```text
deokhugam.infra.redis
├── common
├── config
├── pubsub
├── session
└── ranking
```

### `session`

세션 캐시, 인증 관련 임시 데이터 저장을 담당한다.

예시 클래스:

- `SessionRedisRepository`
- `SessionCacheService`
- `SessionDto`

주요 특징:

- key-value 저장
- TTL 필수
- 조회/삭제 빈도 높음

### `ranking`

인기 도서, 인기 리뷰, 파워유저 같은 실시간 순위를 담당한다.

예시 클래스:

- `RankingRedisRepository`
- `RankingService`
- `RankingSnapshotScheduler`

주요 특징:

- Redis `ZSet` 사용
- 점수 증가/감소
- 상위 N개 조회
- 일정 주기마다 DB 스냅샷 저장 가능

## 현재 pub/sub 흐름

현재 구현된 pub/sub 흐름은 아래와 같다.

1. `RedisPublisher`가 `notification` 채널에 메시지를 발행한다.
2. `RedisPubSubConfig`에 등록된 `RedisMessageListenerContainer`가 메시지를 수신한다.
3. `RedisSubscriber`가 메시지를 받아 처리한다.
4. 현재는 로그만 남기지만, 이후 실제 알림 로직으로 확장할 수 있다.

## 정리

현재 `deokhugam.infra.redis`는 Redis를 목적별로 나누어 관리하기 위한 시작 구조다.

- `config`: Redis Bean 설정
- `common`: 공통 key/TTL 규칙
- `pubsub`: 실시간 메시지 발행/구독

이후 기능이 늘어나면 `session`, `ranking`을 분리해서 추가하는 방향으로 확장한다.
