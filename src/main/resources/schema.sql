-- 1. スキーマ作成
CREATE DATABASE IF NOT EXISTS springec001 CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
USE springec001;

DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

-- 2. ユーザーテーブル
CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- 3. 商品テーブル
CREATE TABLE IF NOT EXISTS items (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    description TEXT,
    stock INT NOT NULL DEFAULT 0
);

-- 4. カートテーブル（ユーザーごとに1つのカートを持つイメージ）
CREATE TABLE IF NOT EXISTS carts (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 5. カート詳細テーブル（カートの中身）
CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

-- 6. 注文親テーブル
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_price INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 7. 注文明細テーブル
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price_at_order INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);

-- 【テストデータ投入】
INSERT INTO users (username, password, email) VALUES ('ieneko', '$2a$10$JQntGgcCZF57OWOQwXSU0.lCSrhFUpZ51op.KFctlDdqBjSxVCJoi', 'ieneko@example.com');
INSERT INTO items (name, price, description, stock) VALUES 
('JavaマスターTシャツ', 2500, 'スレッドセーフな着心地。', 10),
('Spring Bootマグカップ', 1200, 'DI（注ぎ込み）が捗る。', 5),
('MySQLの鍵', 5000, 'データ守護の証。', 2);