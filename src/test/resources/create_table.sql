USE testdb;

-- テスト用テーブル作成SQL
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    age INTEGER NOT NULL,
    created_at DATETIME NOT NULL
);

-- インデックスの作成
-- CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
-- CREATE INDEX IF NOT EXISTS idx_users_age ON users(age);
-- CREATE INDEX IF NOT EXISTS idx_users_name ON users(name);