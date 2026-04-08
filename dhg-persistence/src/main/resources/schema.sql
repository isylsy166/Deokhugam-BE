-- trigram 기반 부분 검색/유사 검색 인덱스를 만들기 위해 pg_trgm 확장 활성화
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE users (
    id bigint NOT NULL  primary key ,
    email varchar(50) NOT NULL,
    nickname varchar(10) NOT NULL,
    password varchar(20) NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NULL,
    deleted_at timestamp NULL,
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE social_account (
    id varchar(255) NOT NULL primary key,
    user_id bigint NOT NULL,
    provider varchar(255) NOT NULL,
    provider_user_id varchar(255) NOT NULL,
    CONSTRAINT uq_social_account_provider_provider_user_id UNIQUE (provider, provider_user_id)
);

CREATE TABLE review_likes (
    id bigint NOT NULL primary key,
    review_id bigint NOT NULL,
    user_id bigint NOT NULL,
    created_at timestamp NULL,
    updated_at timestamp NULL
);

CREATE TABLE reviews (
    id bigint NOT NULL  primary key,
    user_id bigint NOT NULL,
    book_id bigint NOT NULL,
    content varchar(1024) NULL,
    rating integer NOT NULL,
    created_at timestamp NULL,
    updated_at timestamp NULL,
    deleted_at timestamp NULL
);

CREATE TABLE notifications (
    id bigint NOT NULL  primary key,
    review_id bigint NULL,
    user_id bigint NOT NULL,
    type varchar(10) NULL,
    content varchar(1024) NULL,
    confirmed boolean NULL,
    created_at timestamp NULL,
    updated_at timestamp NULL
);

CREATE TABLE comments (
    id bigint NOT NULL  PRIMARY KEY ,
    user_id bigint NOT NULL,
    review_id bigint NOT NULL,
    content varchar(1024) NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NULL,
    deleted_at timestamp NULL
);

CREATE TABLE books (
    id bigint NOT NULL  PRIMARY KEY ,
    title varchar(256) NULL,
    author varchar(256) NULL,
    description varchar(1024) NULL,
    publisher varchar(256) NULL,
    published_at timestamp NULL,
    isbn varchar(1024) NULL,
    thumbnail_url varchar(1024) NULL,
    created_at timestamp NULL,
    updated_at timestamp NULL,
    deleted_at timestamp NULL,
    CONSTRAINT uq_books_isbn UNIQUE (isbn)
);

CREATE TABLE rank_snapshot (
    id bigint NOT NULL  PRIMARY KEY ,
    target_type varchar(255) NULL,
    target_id varchar(255) NULL,
    period_type varchar(255) NULL,
    period_start varchar(255) NULL,
    period_end varchar(255) NULL,
    rank_no varchar(255) NULL,
    score varchar(255) NULL,
    created_at varchar(255) NULL
);



-- title ILIKE '%keyword%' 같은 포함 검색이나 오타에 가까운 유사 문자열 검색 성능을 높이는 GIN 인덱스
CREATE INDEX idx_books_title_trgm ON books USING gin (title gin_trgm_ops);

CREATE INDEX idx_books_author ON books (author);



