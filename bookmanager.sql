-- 创建数据库
CREATE DATABASE IF NOT EXISTS bookmanager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bookmanager;

-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `nickname` VARCHAR(50) DEFAULT NULL,
  `avatar` VARCHAR(255) DEFAULT NULL,
  `phone` VARCHAR(20) DEFAULT NULL,
  `role` VARCHAR(20) NOT NULL DEFAULT 'user',
  `status` INT NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建图书分类表
CREATE TABLE IF NOT EXISTS `category` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL,
  `parent_id` INT NOT NULL DEFAULT 0,
  `description` TEXT DEFAULT NULL,
  `sort_order` INT NOT NULL DEFAULT 0,
  `status` INT NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建图书表
CREATE TABLE IF NOT EXISTS `book` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(200) NOT NULL,
  `author` VARCHAR(100) NOT NULL,
  `publisher` VARCHAR(100) DEFAULT NULL,
  `isbn` VARCHAR(50) NOT NULL UNIQUE,
  `category_id` INT NOT NULL,
  `cover_image` VARCHAR(255) DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `publish_year` INT DEFAULT NULL,
  `total_stock` INT NOT NULL DEFAULT 0,
  `available_stock` INT NOT NULL DEFAULT 0,
  `borrow_count` INT NOT NULL DEFAULT 0,
  `status` INT NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建借阅记录表
CREATE TABLE IF NOT EXISTS `borrow_record` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `user_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  `borrow_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expected_return_date` DATETIME NOT NULL,
  `actual_return_date` DATETIME DEFAULT NULL,
  `borrow_days` INT NOT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'pending',
  `reject_reason` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入管理员账号 (密码: 123456, 加密方式: BCrypt)
INSERT INTO `user` (`username`, `email`, `password`, `nickname`, `role`, `status`) VALUES
('admin', 'admin@bookmanager.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '管理员', 'admin', 1);

-- 插入默认分类
INSERT INTO `category` (`name`, `parent_id`, `description`, `sort_order`) VALUES
('计算机科学', 0, '计算机相关图书', 1),
('文学', 0, '文学作品', 2),
('历史', 0, '历史书籍', 3),
('小说', 2, '小说类图书', 1),
('传记', 2, '人物传记', 2),
('编程', 1, '编程相关图书', 1),
('算法', 1, '算法相关图书', 2);

-- 插入默认图书
INSERT INTO `book` (`title`, `author`, `publisher`, `isbn`, `category_id`, `description`, `publish_year`, `total_stock`, `available_stock`) VALUES
('Java核心技术', 'Cay S. Horstmann', '机械工业出版社', '9787111676614', 6, 'Java经典教材', 2020, 10, 10),
('算法导论', 'Thomas H. Cormen', '机械工业出版社', '9787111407010', 7, '算法经典教材', 2013, 8, 8),
('百年孤独', '加西亚·马尔克斯', '南海出版公司', '9787544253994', 4, '魔幻现实主义经典', 2011, 15, 15),
('人类简史', '尤瓦尔·赫拉利', '中信出版社', '9787508647357', 3, '人类历史概述', 2014, 12, 12),
('活着', '余华', '作家出版社', '9787506360981', 4, '中国当代小说', 2012, 20, 20);