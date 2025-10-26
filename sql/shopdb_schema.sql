-- MySQL schema for Shop Management System
CREATE DATABASE IF NOT EXISTS shopdb DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopdb;


-- users
CREATE TABLE IF NOT EXISTS users (
user_id VARCHAR(50) PRIMARY KEY,
username VARCHAR(100) UNIQUE NOT NULL,
password_hash VARCHAR(256) NOT NULL,
role VARCHAR(20) NOT NULL
) ENGINE=InnoDB;


-- products
CREATE TABLE IF NOT EXISTS products (
id VARCHAR(50) PRIMARY KEY,
name VARCHAR(200) NOT NULL,
quantity INT NOT NULL,
price DOUBLE NOT NULL
) ENGINE=InnoDB;


-- customers
CREATE TABLE IF NOT EXISTS customers (
id VARCHAR(50) PRIMARY KEY,
name VARCHAR(200) NOT NULL,
phone VARCHAR(50)
) ENGINE=InnoDB;


-- bills
CREATE TABLE IF NOT EXISTS bills (
bill_id VARCHAR(50) PRIMARY KEY,
customer_id VARCHAR(50),
total DOUBLE NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (customer_id) REFERENCES customers(id)
) ENGINE=InnoDB;


-- bill items
CREATE TABLE IF NOT EXISTS bill_items (
id INT AUTO_INCREMENT PRIMARY KEY,
bill_id VARCHAR(50) NOT NULL,
product_id VARCHAR(50) NOT NULL,
qty INT NOT NULL,
FOREIGN KEY (bill_id) REFERENCES bills(bill_id) ON DELETE CASCADE,
FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB;


-- grant example user (run as root separately)
-- CREATE USER 'shop_user'@'localhost' IDENTIFIED BY 'shop_pass';
-- GRANT ALL PRIVILEGES ON shopdb.* TO 'shop_user'@'localhost';


-- Insert a default admin user (password: admin123) - hash the password using SHA-256 before inserting
-- Example hash for 'admin123' (sha256): ef92b778bafe771e89245b89ecbcf32f6a0c3a8b0c529d39a9f6f0f5d9d3a1f3
INSERT IGNORE INTO users (user_id, username, password_hash, role) VALUES ('u_admin','admin','ef92b778bafe771e89245b89ecbcf32f6a0c3a8b0c529d39a9f6f0f5d9d3a1f3','ADMIN');