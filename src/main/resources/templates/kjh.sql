-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        11.4.3-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- kjh 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `kjh` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `kjh`;

-- 테이블 kjh.board 구조 내보내기
CREATE TABLE IF NOT EXISTS `board` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` mediumtext NOT NULL,
  `writer` varchar(100) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `original_filename` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 kjh.board:~2 rows (대략적) 내보내기
INSERT INTO `board` (`id`, `title`, `content`, `writer`, `filename`, `original_filename`, `created_at`) VALUES
	(1, 'test', 'test', '익명', '40a11c6f-3d7e-4dd1-b5c4-749d91e74228_세액공제 5. 배당세액공제 검토서식_2022년.hwp', '세액공제 5. 배당세액공제 검토서식_2022년.hwp', '2025-05-29 09:54:28'),
	(2, 'test', 'test', 'test', '82c9a40b-647d-4371-8a1c-4936655f1c1b_기본이력서워드[템프인]+동의서.doc', '기본이력서워드[템프인]+동의서.doc', '2025-05-31 06:08:43'),
	(3, 'test', 'test', 'test', NULL, NULL, '2025-08-28 07:33:14');

-- 테이블 kjh.board2 구조 내보내기
CREATE TABLE IF NOT EXISTS `board2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` text NOT NULL,
  `writer` varchar(100) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `filename` varchar(255) DEFAULT NULL,
  `original_filename` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 kjh.board2:~11 rows (대략적) 내보내기
INSERT INTO `board2` (`id`, `title`, `content`, `writer`, `created_at`, `filename`, `original_filename`) VALUES
	(2, 'test', 'test', 'test', '2025-11-19 17:26:59', NULL, NULL),
	(3, 'test', 'testtest', 'test', '2025-11-19 17:27:08', NULL, NULL),
	(4, 'testtest', 'setssetset', 'test', '2025-11-19 17:27:13', NULL, NULL),
	(5, 'testse', 'tsetset', 'test', '2025-11-19 17:27:18', NULL, NULL),
	(6, 'testse', 'setset', 'test', '2025-11-19 17:27:22', NULL, NULL),
	(7, 'test', 'setsetse', 'test', '2025-11-19 17:27:25', NULL, NULL),
	(8, 'testses', 'testsets', 'test', '2025-11-19 17:27:29', NULL, NULL),
	(9, 'tests', 'stestse', 'test', '2025-11-19 17:27:33', NULL, NULL),
	(10, 'testsetse', 'tsetsetes', 'test', '2025-11-19 17:27:38', NULL, NULL),
	(11, 'testse', 'testsetse', 'test', '2025-11-19 17:27:41', NULL, NULL),
	(12, 'testsetsete', 'tsetsetsete', 'test', '2025-11-19 17:27:45', NULL, NULL);

-- 테이블 kjh.member 구조 내보내기
CREATE TABLE IF NOT EXISTS `member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 kjh.member:~2 rows (대략적) 내보내기
INSERT INTO `member` (`id`, `username`, `password`) VALUES
	(1, 'test', 'test'),
	(2, 'test2', 'test2');

-- 테이블 kjh.member2 구조 내보내기
CREATE TABLE IF NOT EXISTS `member2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 kjh.member2:~0 rows (대략적) 내보내기
INSERT INTO `member2` (`id`, `username`, `password`, `name`) VALUES
	(1, 'test', 'test', 'test');

-- 테이블 kjh.post 구조 내보내기
CREATE TABLE IF NOT EXISTS `post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `content` longtext DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- 테이블 데이터 kjh.post:~0 rows (대략적) 내보내기

-- 테이블 kjh.test 구조 내보내기
CREATE TABLE IF NOT EXISTS `test` (
  `test_id` int(11) NOT NULL AUTO_INCREMENT,
  `test_contents` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- 테이블 데이터 kjh.test:~0 rows (대략적) 내보내기

-- 테이블 kjh.user1 구조 내보내기
CREATE TABLE IF NOT EXISTS `user1` (
  `id` varchar(255) NOT NULL,
  `age` int(11) NOT NULL,
  `hp` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `uid` varchar(255) NOT NULL,
  `pass` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- 테이블 데이터 kjh.user1:~0 rows (대략적) 내보내기

-- 테이블 kjh.user2 구조 내보내기
CREATE TABLE IF NOT EXISTS `user2` (
  `id` varchar(255) NOT NULL,
  `age` int(11) NOT NULL,
  `hp` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `uid` varchar(255) NOT NULL,
  `pass` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- 테이블 데이터 kjh.user2:~0 rows (대략적) 내보내기

-- 테이블 kjh.users 구조 내보내기
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- 테이블 데이터 kjh.users:~0 rows (대략적) 내보내기

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
