-- 파일: db/create_accommodation_like.sql
-- 설명: 애플리케이션에서 사용 중인 `accommodation_like` 테이블이 없어서 에러가 발생했습니다.
-- 아래 SQL을 DB(스키마: airbnb)에 실행하면 테이블이 생성되며, FK 제약은 환경에 맞게 활성화/비활성화 하세요.

-- 1) 권장: FK 포함(운영/테스트에서 관계 무결성 유지)
CREATE TABLE IF NOT EXISTS accommodation_like (
  id INT AUTO_INCREMENT PRIMARY KEY,
  accommodation_id INT NOT NULL,
  account_id VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_accommodation_id (accommodation_id),
  INDEX idx_account_id (account_id),
  CONSTRAINT fk_accommodation_like_accommodation FOREIGN KEY (accommodation_id) REFERENCES accommodation(id) ON DELETE CASCADE,
  CONSTRAINT fk_accommodation_like_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2) (대안) FK 없이 단순 테이블(빠른 테스트용)
-- CREATE TABLE IF NOT EXISTS accommodation_like (
--   id INT AUTO_INCREMENT PRIMARY KEY,
--   accommodation_id INT NOT NULL,
--   account_id VARCHAR(255) NOT NULL,
--   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--   INDEX idx_accommodation_id (accommodation_id),
--   INDEX idx_account_id (account_id)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3) 테스트용 데이터(테이블 생성 후 실행)
-- INSERT INTO accommodation_like (accommodation_id, account_id) VALUES (9, 'user-1');

-- 4) 확인 쿼리
-- SHOW TABLES LIKE 'accommodation_like';
-- DESCRIBE accommodation_like;
-- SELECT * FROM accommodation_like LIMIT 10;

-- 주의:
-- * account.id와 accommodation.id 컬럼 타입/길이가 다르면 account_id 타입을 조정하세요.
-- * 원격 DB(RDS)에 접속하려면 mysql 클라이언트 혹은 관리 콘솔을 사용하세요.
-- 접속 예시 (macOS, zsh):
-- mysql -h <HOST> -P <PORT> -u <USER> -p
-- USE airbnb;
-- SOURCE /absolute/path/to/create_accommodation_like.sql;  -- 또는 파일 내용 복붙 후 실행

