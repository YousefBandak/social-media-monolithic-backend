-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 05, 2024 at 11:35 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `oop`
--

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE `comment` (
  `content_id` bigint(20) NOT NULL,
  `contentType` enum('SharedPost','Post','Comment') DEFAULT NULL,
  `privacy` enum('PUBLIC','FRIENDS','PRIVATE') DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `contentAuthor_username` varchar(255) DEFAULT NULL,
  `numOfComments` int(11) NOT NULL,
  `numOfReactions` int(11) NOT NULL,
  `textData` varchar(2000) DEFAULT NULL,
  `commented_on` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`content_id`, `contentType`, `privacy`, `timestamp`, `contentAuthor_username`, `numOfComments`, `numOfReactions`, `textData`, `commented_on`) VALUES
(52, 'Comment', NULL, '2024-06-04 10:47:43.000000', 'ObjectOrienters', 0, 0, 'hi my first comment!', 6),
(67, 'Comment', NULL, '2024-06-04 11:57:28.000000', 'nadineabuodeh', 0, 0, 'ffff', 1),
(70, 'Comment', NULL, '2024-06-04 12:54:57.000000', 'George', 0, 0, 'رائع جداً', 1);

-- --------------------------------------------------------

--
-- Table structure for table `content_seq`
--

CREATE TABLE `content_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `content_seq`
--

INSERT INTO `content_seq` (`next_val`) VALUES
(251);

-- --------------------------------------------------------

--
-- Table structure for table `data_types`
--

CREATE TABLE `data_types` (
  `datatype_id` bigint(20) NOT NULL,
  `fileName` varchar(255) DEFAULT NULL,
  `fileUrl` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `data_types`
--

INSERT INTO `data_types` (`datatype_id`, `fileName`, `fileUrl`, `type`, `content_id`) VALUES
(1, '1717450003633_Technology Background Video Loop For Website.mp4', 'http://localhost:8080/media_uploads/1717450003633_Technology%20Background%20Video%20Loop%20For%20Website.mp4', 'video/mp4', 3),
(2, '1717450166323_360_F_308697506_9dsBYHXm9FwuW0qcEqimAEXUvzTwfzwe.jpg', 'http://localhost:8080/media_uploads/1717450166323_360_F_308697506_9dsBYHXm9FwuW0qcEqimAEXUvzTwfzwe.jpg', 'image/jpeg', 4),
(3, '1717450166325_AI.jpg', 'http://localhost:8080/media_uploads/1717450166325_AI.jpg', 'image/jpeg', 4),
(4, '1717450269855_react.jpg', 'http://localhost:8080/media_uploads/1717450269855_react.jpg', 'image/jpeg', 5),
(5, '1717450382629_Rest-API-Large.png', 'http://localhost:8080/media_uploads/1717450382629_Rest-API-Large.png', 'image/png', 6),
(6, '1717450382631_spring-framework.png', 'http://localhost:8080/media_uploads/1717450382631_spring-framework.png', 'image/png', 6),
(7, '1717452557574_bgjpg.jpg', 'http://localhost:8080/media_uploads/1717452557574_bgjpg.jpg', 'image/jpeg', NULL),
(8, '1717452595499_AlQuds.jpg', 'http://localhost:8080/media_uploads/1717452595499_AlQuds.jpg', 'image/jpeg', NULL),
(9, '1717452629814_Nursing sim.png', 'http://localhost:8080/media_uploads/1717452629814_Nursing%20sim.png', 'image/png', NULL),
(10, '1717452655123_spring-framework.png', 'http://localhost:8080/media_uploads/1717452655123_spring-framework.png', 'image/png', NULL),
(11, '1717453071081_images.jpg', 'http://localhost:8080/media_uploads/1717453071081_images.jpg', 'image/jpeg', NULL),
(12, '1717453170327_1692628559941.png', 'http://localhost:8080/media_uploads/1717453170327_1692628559941.png', 'image/png', NULL),
(13, '1717453229867_software-engineering-resume-writin.2e16d0ba.fill-480x480.jpg', 'http://localhost:8080/media_uploads/1717453229867_software-engineering-resume-writin.2e16d0ba.fill-480x480.jpg', 'image/jpeg', NULL),
(14, '1717491751805_1692628559941.png', 'http://localhost:8080/media_uploads/1717491751805_1692628559941.png', 'image/png', NULL),
(15, '1720171623715_IMG_8093.JPG', 'http://localhost:8080/media_uploads/1720171623715_IMG_8093.JPG', 'image/jpeg', 152),
(16, '1720171908821_wallpaperflare.com_wallpaper2.jpg', 'http://localhost:8080/media_uploads/1720171908821_wallpaperflare.com_wallpaper2.jpg', 'image/jpeg', NULL),
(17, '1720171919027_wallpaperflare.com_wallpaper.jpg', 'http://localhost:8080/media_uploads/1720171919027_wallpaperflare.com_wallpaper.jpg', 'image/jpeg', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `followship`
--

CREATE TABLE `followship` (
  `follower_id` varchar(255) NOT NULL,
  `following_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `followship`
--

INSERT INTO `followship` (`follower_id`, `following_id`) VALUES
('AbuAyyash', 'Anas'),
('Anas', 'AbuAyyash'),
('George', 'Yousef2003'),
('Joseph', 'Nader'),
('nadineabuodeh', 'Angela2003'),
('nadineabuodeh', 'Yousef2003'),
('ObjectOrienters', 'Elias'),
('ObjectOrienters', 'Joseph'),
('ObjectOrienters', 'Nader'),
('ObjectOrienters', 'Rawan'),
('ObjectOrienters', 'Suhail'),
('ObjectOrienters', 'Yousef2003'),
('Rawan', 'Shireen'),
('Suhail', 'Husam2003'),
('Tala2001', 'Angela2003'),
('Yasmeen', 'Suhail'),
('Yousef2003', 'Angela2003');

-- --------------------------------------------------------

--
-- Table structure for table `post`
--

CREATE TABLE `post` (
  `content_id` bigint(20) NOT NULL,
  `contentType` enum('SharedPost','Post','Comment') DEFAULT NULL,
  `privacy` enum('PUBLIC','FRIENDS','PRIVATE') DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `contentAuthor_username` varchar(255) DEFAULT NULL,
  `numOfComments` int(11) NOT NULL,
  `numOfReactions` int(11) NOT NULL,
  `textData` varchar(2000) DEFAULT NULL,
  `numOfShares` int(11) NOT NULL,
  `tags` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `post`
--

INSERT INTO `post` (`content_id`, `contentType`, `privacy`, `timestamp`, `contentAuthor_username`, `numOfComments`, `numOfReactions`, `textData`, `numOfShares`, `tags`) VALUES
(1, 'Post', 'PUBLIC', '2024-06-04 00:24:23.000000', 'Yousef2003', 2, 2, '# java\r\nThis is my java command \r\n``` java\r\npublic class FactorialCalculator {\r\n    // Function to calculate factorial\r\n    public static int factorial(int n) {\r\n        if (n == 0) {\r\n            return 1;\r\n        } else {\r\n            return n * factorial(n - 1);\r\n        }\r\n    }\r\n    public static void main(String[] args) {\r\n        int number = 5; // Example number\r\n        int result = factorial(number);\r\n        System.out.println(\"Factorial of \" + number + \" is \" + result);\r\n    }\r\n}\r\n```\r\njava \r\n> By Yousef Albandak', 2, ''),
(2, 'Post', 'PUBLIC', '2024-06-04 00:26:01.000000', 'Suhail', 0, 0, '# java\nThis is my java command \n``` java\npublic class FactorialCalculator {\n    // Function to calculate factorial\n    public static int factorial(int n) {\n        if (n == 0) {\n            return 1;\n        } else {\n            return n * factorial(n - 1);\n        }\n    }\n    public static void main(String[] args) {\n        int number = 5; // Example number\n        int result = factorial(number);\n        System.out.println(\"Factorial of \" + number + \" is \" + result);\n    }\n}\n```\njava \n> By Angela Salem', 0, ''),
(3, 'Post', 'PUBLIC', '2024-06-04 00:26:43.000000', 'Suhail', 0, 0, 'Watch this new Tech video! #Tech #Software #SoftwareEngineering', 0, 'softwareengineering,tech,software'),
(4, 'Post', 'PUBLIC', '2024-06-04 00:29:26.000000', 'Rawan', 0, 1, 'AI will take over the world! #AI #MachineLearning', 0, 'machinelearning,ai'),
(5, 'Post', 'PUBLIC', '2024-06-04 00:31:09.000000', 'Anas', 0, 1, 'React is a great Frontend library! #React #Frontend', 0, 'frontend,react'),
(6, 'Post', 'PUBLIC', '2024-06-04 00:33:02.000000', 'Joseph', 1, 1, 'Springboot with Rest API is amazing! #Spring #Springboot #RestAPI', 0, 'springboot,spring,restapi'),
(53, 'Post', 'PUBLIC', '2024-06-04 11:06:15.000000', 'ObjectOrienters', 0, 0, '#RestAPI', 0, 'restapi'),
(54, 'Post', 'PUBLIC', '2024-06-04 11:06:15.000000', 'ObjectOrienters', 0, 0, '#RestAPI', 0, 'restapi'),
(55, 'Post', 'PUBLIC', '2024-06-04 11:06:16.000000', 'ObjectOrienters', 0, 0, '#RestAPI', 0, 'restapi'),
(56, 'Post', 'PUBLIC', '2024-06-04 11:06:16.000000', 'ObjectOrienters', 0, 0, '#RestAPI', 0, 'restapi'),
(57, 'Post', 'PUBLIC', '2024-06-04 11:06:16.000000', 'ObjectOrienters', 0, 0, '#RestAPI', 0, 'restapi'),
(58, 'Post', 'PUBLIC', '2024-06-04 11:06:16.000000', 'ObjectOrienters', 0, 0, '#RestAPI', 0, 'restapi'),
(59, 'Post', 'PUBLIC', '2024-06-04 11:06:16.000000', 'ObjectOrienters', 0, 0, '#RestAPI', 0, 'restapi'),
(60, 'Post', 'PUBLIC', '2024-06-04 11:06:18.000000', 'ObjectOrienters', 0, 0, '#RestAPI', 0, 'restapi'),
(61, 'Post', 'PUBLIC', '2024-06-04 11:06:18.000000', 'ObjectOrienters', 0, 0, '#RestAPI', 0, 'restapi'),
(62, 'Post', 'PUBLIC', '2024-06-04 11:06:18.000000', 'ObjectOrienters', 0, 0, '#RestAPI', 0, 'restapi'),
(63, 'Post', 'PUBLIC', '2024-06-04 11:06:18.000000', 'ObjectOrienters', 0, 0, '#RestAPI', 0, 'restapi'),
(64, 'Post', 'PUBLIC', '2024-06-04 11:39:19.000000', 'ObjectOrienters', 0, 0, '``` java \r\npublic static void main(String[] args) {\r\n        int number = 5; // Example number\r\n        int result = factorial(number);\r\n        System.out.println(\"Factorial of \" + number + \" is \" + result);\r\n    }\r\n```\r\n> Tala', 0, ''),
(65, 'Post', 'PUBLIC', '2024-06-04 11:39:20.000000', 'ObjectOrienters', 0, 0, '``` java \r\npublic static void main(String[] args) {\r\n        int number = 5; // Example number\r\n        int result = factorial(number);\r\n        System.out.println(\"Factorial of \" + number + \" is \" + result);\r\n    }\r\n```\r\n> Tala', 0, ''),
(66, 'Post', 'PUBLIC', '2024-06-04 11:57:05.000000', 'nadineabuodeh', 0, 0, 'hello', 0, ''),
(69, 'Post', 'PUBLIC', '2024-06-04 12:50:49.000000', 'ObjectOrienters', 0, 0, '# Heading \r\n\r\nThis is my first post', 0, ''),
(72, 'Post', 'PUBLIC', '2024-06-04 12:56:46.000000', 'George', 0, 0, '``` java\r\n\r\npublic static int x = 10;\r\n```\r\n\r\n> By George', 0, ''),
(102, 'Post', 'PUBLIC', '2024-06-06 11:58:27.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(103, 'Post', 'PUBLIC', '2024-06-06 11:58:27.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(104, 'Post', 'PUBLIC', '2024-06-06 11:58:27.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(105, 'Post', 'PUBLIC', '2024-06-06 11:58:27.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(106, 'Post', 'PUBLIC', '2024-06-06 11:58:28.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(107, 'Post', 'PUBLIC', '2024-06-06 11:58:29.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(108, 'Post', 'PUBLIC', '2024-06-06 11:58:29.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(109, 'Post', 'PUBLIC', '2024-06-06 11:58:29.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(110, 'Post', 'PUBLIC', '2024-06-06 11:58:30.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(111, 'Post', 'PUBLIC', '2024-06-06 11:58:30.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(112, 'Post', 'PUBLIC', '2024-06-06 11:58:32.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(113, 'Post', 'PUBLIC', '2024-06-06 11:58:32.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(114, 'Post', 'PUBLIC', '2024-06-06 11:58:32.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(115, 'Post', 'PUBLIC', '2024-06-06 11:58:32.000000', 'ObjectOrienters', 0, 0, '#Spring ', 0, 'spring'),
(152, 'Post', 'PUBLIC', '2024-07-05 12:27:03.000000', 'ObjectOrienters', 0, 0, 'hi my name is tala ', 0, '');

-- --------------------------------------------------------

--
-- Table structure for table `profile`
--

CREATE TABLE `profile` (
  `username` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `about` varchar(255) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `name` varchar(30) NOT NULL,
  `profession` varchar(255) DEFAULT NULL,
  `background_img_datatype_id` bigint(20) DEFAULT NULL,
  `owner_username` varchar(255) DEFAULT NULL,
  `profile_pic_datatype_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `profile`
--

INSERT INTO `profile` (`username`, `email`, `about`, `dob`, `gender`, `name`, `profession`, `background_img_datatype_id`, `owner_username`, `profile_pic_datatype_id`) VALUES
('AbuAyyash', 'mohammed@bethlehem.edu', 'Software Engineering Doctor at Bethlehem University', NULL, 0, 'Mohammed AbuAyyash', 'Software Engineer', 7, 'AbuAyyash', 13),
('Anas', 'anas@bethlehem.edu', 'Software Engineering Doctor at Bethlehem University', NULL, 0, 'Anas Samara', 'Software Engineer', NULL, 'Anas', NULL),
('Angela2003', 'angela@bethlehem.edu', 'Software Engineering student at BU.', NULL, 1, 'Angela Salem', 'Software Engineer', NULL, 'Angela2003', NULL),
('Elias', 'object.orienters@gmail.com', NULL, NULL, NULL, 'Object Orienter', NULL, NULL, 'Elias', NULL),
('George', '202302791@bethlehem.edu', NULL, NULL, NULL, 'Sleibi', NULL, NULL, 'George', NULL),
('Husam2003', 'husam@bethlehem.edu', 'Software Engineering student at BU.', NULL, 0, 'Husam Ramoni', 'Software Engineer', 9, 'Husam2003', NULL),
('Joseph', 'joseph@bethlehem.edu', 'Software Engineer at Bethlehem University.', NULL, 0, 'Joseph Hodali', 'Software Engineer', NULL, 'Joseph', NULL),
('Nader', 'nader@bethlehem.edu', 'Software Engineering Doctor at Bethlehem University', NULL, 0, 'Nader Abu Saad', 'Software Engineer', 8, 'Nader', 12),
('nadineabuodeh', 'nadineabuodeh4@gmail.com', NULL, NULL, NULL, 'nadine abuodeh', NULL, NULL, 'nadineabuodeh', NULL),
('ObjectOrienters', 'objectorienters@gmail.com', NULL, NULL, NULL, 'Object Orienter', NULL, 16, 'ObjectOrienters', 17),
('Rawan', 'rawan@bethlehem.edu', 'Software Engineering Doctor at Bethlehem University', NULL, 1, 'Rawan Gedeon', 'Software Engineer', NULL, 'Rawan', NULL),
('Shireen', 'shireen@bethlehem.edu', 'Software Engineering Doctor at Bethlehem University', NULL, 1, 'Shireen Hazboun', 'Software Engineer', 10, 'Shireen', NULL),
('Suhail', 'suhail@bethlehem.edu', 'Chair of Software Engineering at BU.', NULL, 0, 'Suhail Odeh', 'Software Engineer', NULL, 'Suhail', NULL),
('Tala2001', 'tala@bethlehem.edu', 'Im an intern in BU', '2001-12-12', 1, 'Tala Aldibs', 'Software Engineer', NULL, 'Tala2001', NULL),
('Yasmeen', 'yasmeen@gmail.com', NULL, NULL, NULL, 'Yasmeen Yameen', NULL, NULL, 'Yasmeen', 14),
('Yousef2003', 'yousef@bethlehem.edu', 'Software Engineering student at BU.', NULL, 0, 'Yousef Albandak', 'Software Engineer', NULL, 'Yousef2003', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `reaction`
--

CREATE TABLE `reaction` (
  `reactionID` varchar(255) NOT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `type` enum('LIKE','DISLIKE','LOVE','SUPPORT','HAHA') NOT NULL,
  `content_id` bigint(20) NOT NULL,
  `reactor_username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reaction`
--

INSERT INTO `reaction` (`reactionID`, `timestamp`, `type`, `content_id`, `reactor_username`) VALUES
('George1', '2024-06-04 12:54:42.000000', 'LIKE', 1, 'George'),
('nadineabuodeh1', '2024-06-04 11:57:22.000000', 'LOVE', 1, 'nadineabuodeh'),
('ObjectOrienters4', '2024-07-05 12:25:27.000000', 'LOVE', 4, 'ObjectOrienters'),
('ObjectOrienters5', '2024-06-04 11:05:20.000000', 'LIKE', 5, 'ObjectOrienters'),
('ObjectOrienters6', '2024-06-04 12:46:49.000000', 'SUPPORT', 6, 'ObjectOrienters');

-- --------------------------------------------------------

--
-- Table structure for table `refreshtoken`
--

CREATE TABLE `refreshtoken` (
  `id` bigint(20) NOT NULL,
  `expiryDate` datetime(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `refreshtoken`
--

INSERT INTO `refreshtoken` (`id`, `expiryDate`, `token`, `username`) VALUES
(1, '2024-07-06 09:17:34.000000', 'b19f545f-d70d-41a3-bab3-80cec2e5daa2', 'Angela2003'),
(2, '2024-07-06 09:18:03.000000', '429167e3-bfba-4ae1-94ac-ed42c1d1eaae', 'Yousef2003'),
(3, '2024-06-04 22:03:12.000000', '7b694959-061f-4ea0-a256-6904ba5e4ceb', 'Suhail'),
(4, '2024-06-04 22:04:06.000000', 'efa5342d-eb76-4ada-82c1-831c687a249f', 'Rawan'),
(5, '2024-06-04 22:04:36.000000', '45683f33-f73e-42d7-b7fd-4cfeb02e419a', 'Anas'),
(6, '2024-06-04 22:02:23.000000', '17fa7247-b5ba-4c68-88c4-671ac5d49a2d', 'Joseph'),
(52, '2024-06-05 07:39:12.000000', 'd649e5cb-b16e-4c3f-a1c0-605cabd51712', 'AbuAyyash'),
(53, '2024-06-04 22:19:22.000000', '66aca6e7-6db5-4055-a002-9e6f0d5e4fe2', 'Nader'),
(54, '2024-06-04 22:10:17.000000', 'fbfa8b4a-37e5-4624-bcb3-bdcf3ca5ab30', 'Husam2003'),
(55, '2024-06-04 22:10:46.000000', '9a7cf6b1-bec9-458e-a4f8-d191b29eaa44', 'Shireen'),
(102, '2024-07-06 09:17:06.000000', '5dfdd8d0-31a3-47b0-b66d-13d6087cdd06', 'Elias'),
(103, '2024-06-05 08:55:45.000000', '9a7eea1f-f6de-404b-a1da-d27e67182d68', 'nadineabuodeh'),
(104, '2024-06-05 09:02:18.000000', '7369fd1d-e7d7-408a-a276-82739177b645', 'Yasmeen'),
(105, '2024-06-05 09:54:18.000000', 'c1a5dc55-9ad0-4050-a2e7-d67e8cfa901f', 'George'),
(202, '2024-07-06 09:18:28.000000', 'a56e47d0-fd57-4c14-9c1b-be9aec7228cf', 'ObjectOrienters'),
(203, '2024-07-06 09:32:42.000000', '410655c6-ef07-4deb-8a8e-da1f4b1e46d2', 'Tala2001');

-- --------------------------------------------------------

--
-- Table structure for table `refreshtoken_seq`
--

CREATE TABLE `refreshtoken_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `refreshtoken_seq`
--

INSERT INTO `refreshtoken_seq` (`next_val`) VALUES
(301);

-- --------------------------------------------------------

--
-- Table structure for table `sharedpost`
--

CREATE TABLE `sharedpost` (
  `content_id` bigint(20) NOT NULL,
  `contentType` enum('SharedPost','Post','Comment') DEFAULT NULL,
  `privacy` enum('PUBLIC','FRIENDS','PRIVATE') DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `contentAuthor_username` varchar(255) DEFAULT NULL,
  `post_content_id` bigint(20) DEFAULT NULL,
  `profile_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sharedpost`
--

INSERT INTO `sharedpost` (`content_id`, `contentType`, `privacy`, `timestamp`, `contentAuthor_username`, `post_content_id`, `profile_id`) VALUES
(71, 'SharedPost', 'PUBLIC', '2024-06-04 12:55:03.000000', 'George', 1, 'George');

-- --------------------------------------------------------

--
-- Table structure for table `tag`
--

CREATE TABLE `tag` (
  `tagName` varchar(255) NOT NULL,
  `posts` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tag`
--

INSERT INTO `tag` (`tagName`, `posts`) VALUES
('ai', '4'),
('frontend', '5'),
('machinelearning', '4'),
('react', '5'),
('restapi', '6,53,54,55,56,57,58,59,60,61,62,63'),
('software', '3,3'),
('softwareengineering', '3'),
('spring', '6,103,104,105,106,107,108,109,110,111,112,113,114,115'),
('springboot', '6'),
('tech', '3,3');

-- --------------------------------------------------------

--
-- Table structure for table `tokenblacklist`
--

CREATE TABLE `tokenblacklist` (
  `token` varchar(255) NOT NULL,
  `blacklistDate` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `username` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `lastLogin` datetime(6) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `provider` enum('LOCAL','GOOGLE','GITHUB') DEFAULT NULL,
  `providerId` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `email`, `lastLogin`, `password`, `provider`, `providerId`) VALUES
('AbuAyyash', 'mohammed@bethlehem.edu', '2024-06-04 10:39:12.000000', '$2a$10$J16DwpEqQGlzlTdqCXzIGOaZKr/dOa1BfirYMHr4LvPhjOKozxX9u', 'LOCAL', NULL),
('Anas', 'anas@bethlehem.edu', '2024-06-04 01:04:36.000000', '$2a$10$O3ZZ5g6F1LZu/YCU2E5Q7.07tqkOG3APS4P3MvKgRDqz/0QAXb1ky', 'LOCAL', NULL),
('Angela2003', 'angela@bethlehem.edu', '2024-07-05 12:17:34.000000', '$2a$10$/VvwXiQwOwJlwIsxxOiXfuYONNSTcY4k1/OCdrXYTyfwQcE7YBKFe', 'LOCAL', NULL),
('Elias', 'object.orienters@gmail.com', NULL, NULL, 'GOOGLE', '104829252220098330811'),
('George', '202302791@bethlehem.edu', '2024-06-04 12:54:18.000000', '$2a$10$HzsQt1sBDnD47T0tmJUscOLgKysiLqANrzDw2CiOHQV2c2BdoSb1y', 'LOCAL', NULL),
('Husam2003', 'husam@bethlehem.edu', '2024-06-04 01:10:17.000000', '$2a$10$a0OBMzwzQUGe8O2Q8n8Uz.b6OVt0jMVUEPm55tYhneom.lG.McJ1q', 'LOCAL', NULL),
('Joseph', 'joseph@bethlehem.edu', '2024-06-04 01:02:23.000000', '$2a$10$THzTjNYHmqmnWJDaE7vnFeThBp/ME4FIH.6jGPUqg1hMfQGdGy8n.', 'LOCAL', NULL),
('Nader', 'nader@bethlehem.edu', '2024-06-04 01:19:22.000000', '$2a$10$SbMCs0lmcUYF3pFfNVpUdOUJRs7fzZbxocCwyrtrDZ6v0R3ogElgK', 'LOCAL', NULL),
('nadineabuodeh', 'nadineabuodeh4@gmail.com', '2024-06-04 11:55:45.000000', '$2a$10$GjdJ/mcBGuj1b4ADbrxXc.8f6pHR0WKPH4I384yQZTQcM7.WcPa/m', 'LOCAL', NULL),
('ObjectOrienters', 'objectorienters@gmail.com', '2024-07-05 12:18:28.000000', '$2a$10$ln/PpV/6LdzO6KzP7v8l1uwB4/PeZ7p/F/0b3kYJ/HP7BqyahTbk.', 'LOCAL', NULL),
('Rawan', 'rawan@bethlehem.edu', '2024-06-04 01:04:06.000000', '$2a$10$hatbH/bkKyoNoLsuggfN9eyvKn0N6l85wAMOkM4RehulW/KB.gCca', 'LOCAL', NULL),
('Shireen', 'shireen@bethlehem.edu', '2024-06-04 01:10:46.000000', '$2a$10$zueSJzprLfXz/Bgjq28sXOJCJ1w6yEBh1KnT9kznoHKgwP2J2XlXW', 'LOCAL', NULL),
('Suhail', 'suhail@bethlehem.edu', '2024-06-04 01:03:12.000000', '$2a$10$1Fib1fCmELzRAzSz08NM6Ojhpqr56fu.U83NGG8yW67jyLeVHqDmW', 'LOCAL', NULL),
('Tala2001', 'tala@bethlehem.edu', '2024-07-05 12:32:42.000000', '$2a$10$FdFANBVT3qBwHdZkRK5bYutS40dpagM7iMkDZaY4SEBCDVk1UhvHy', 'LOCAL', NULL),
('Yasmeen', 'yasmeen@gmail.com', '2024-06-04 12:02:18.000000', '$2a$10$JnMqLljzS3QHvPu7vzYjcev5cp.f/.GO6PdpPtyjnGjQQoMTAl8o2', 'LOCAL', NULL),
('Yousef2003', 'yousef@bethlehem.edu', '2024-07-05 12:18:03.000000', '$2a$10$r43iZuhV6omZvhPhKxsnsOrVd6MztBXqmOs0..VIA2NS3zU/b3EMe', 'LOCAL', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `useroauthtemp`
--

CREATE TABLE `useroauthtemp` (
  `id` varchar(255) NOT NULL,
  `accessToken` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `profilePicUrl` varchar(255) DEFAULT NULL,
  `provider` enum('LOCAL','GOOGLE','GITHUB') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comment`
--
ALTER TABLE `comment`
  ADD PRIMARY KEY (`content_id`),
  ADD KEY `FK_n7my7i3mm83l3lkfb925ow9tk` (`contentAuthor_username`);

--
-- Indexes for table `data_types`
--
ALTER TABLE `data_types`
  ADD PRIMARY KEY (`datatype_id`);

--
-- Indexes for table `followship`
--
ALTER TABLE `followship`
  ADD PRIMARY KEY (`follower_id`,`following_id`),
  ADD KEY `FKadsmggtp7jh7minoy2qpj995c` (`following_id`);

--
-- Indexes for table `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`content_id`),
  ADD KEY `FK_orolwuyxudpehg1aps5mk7r4n` (`contentAuthor_username`);

--
-- Indexes for table `profile`
--
ALTER TABLE `profile`
  ADD PRIMARY KEY (`username`),
  ADD UNIQUE KEY `UK_pd2qcl445jn2814slepow2u4d` (`owner_username`),
  ADD KEY `FK81u822uqmuyd4x2c315xrd742` (`background_img_datatype_id`),
  ADD KEY `FK45gif74mjxmqih5y8ajmcyh5v` (`profile_pic_datatype_id`);

--
-- Indexes for table `reaction`
--
ALTER TABLE `reaction`
  ADD PRIMARY KEY (`reactionID`),
  ADD KEY `FKdwr8sicmhqw7oowmxtpmphvp0` (`reactor_username`);

--
-- Indexes for table `refreshtoken`
--
ALTER TABLE `refreshtoken`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_1ae3mlfb8xoqm6ju5ouqytato` (`token`),
  ADD UNIQUE KEY `UK_psep91c07j3yaxr8f0aamerf9` (`username`);

--
-- Indexes for table `sharedpost`
--
ALTER TABLE `sharedpost`
  ADD PRIMARY KEY (`content_id`),
  ADD KEY `FKbj0foibffv161na6i7iobe3yp` (`post_content_id`),
  ADD KEY `FK1vlbdte5b3v3jb4ivi7cgho5x` (`profile_id`),
  ADD KEY `FK_a626na3lj2oriqilpbo8gkj99` (`contentAuthor_username`);

--
-- Indexes for table `tag`
--
ALTER TABLE `tag`
  ADD PRIMARY KEY (`tagName`);

--
-- Indexes for table `tokenblacklist`
--
ALTER TABLE `tokenblacklist`
  ADD PRIMARY KEY (`token`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`username`),
  ADD UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`);

--
-- Indexes for table `useroauthtemp`
--
ALTER TABLE `useroauthtemp`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `data_types`
--
ALTER TABLE `data_types`
  MODIFY `datatype_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `FK_n7my7i3mm83l3lkfb925ow9tk` FOREIGN KEY (`contentAuthor_username`) REFERENCES `profile` (`username`);

--
-- Constraints for table `followship`
--
ALTER TABLE `followship`
  ADD CONSTRAINT `FKab1088u1mgk339soauj50hyt4` FOREIGN KEY (`follower_id`) REFERENCES `profile` (`username`),
  ADD CONSTRAINT `FKadsmggtp7jh7minoy2qpj995c` FOREIGN KEY (`following_id`) REFERENCES `profile` (`username`);

--
-- Constraints for table `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `FK_orolwuyxudpehg1aps5mk7r4n` FOREIGN KEY (`contentAuthor_username`) REFERENCES `profile` (`username`);

--
-- Constraints for table `profile`
--
ALTER TABLE `profile`
  ADD CONSTRAINT `FK45gif74mjxmqih5y8ajmcyh5v` FOREIGN KEY (`profile_pic_datatype_id`) REFERENCES `data_types` (`datatype_id`),
  ADD CONSTRAINT `FK81u822uqmuyd4x2c315xrd742` FOREIGN KEY (`background_img_datatype_id`) REFERENCES `data_types` (`datatype_id`),
  ADD CONSTRAINT `FKiymmocmupdk7pokbyxtabguux` FOREIGN KEY (`owner_username`) REFERENCES `user` (`username`);

--
-- Constraints for table `reaction`
--
ALTER TABLE `reaction`
  ADD CONSTRAINT `FKdwr8sicmhqw7oowmxtpmphvp0` FOREIGN KEY (`reactor_username`) REFERENCES `profile` (`username`);

--
-- Constraints for table `refreshtoken`
--
ALTER TABLE `refreshtoken`
  ADD CONSTRAINT `FKhrruf8jcus1v850cyhvbpt2pj` FOREIGN KEY (`username`) REFERENCES `user` (`username`);

--
-- Constraints for table `sharedpost`
--
ALTER TABLE `sharedpost`
  ADD CONSTRAINT `FK1vlbdte5b3v3jb4ivi7cgho5x` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`username`),
  ADD CONSTRAINT `FK_a626na3lj2oriqilpbo8gkj99` FOREIGN KEY (`contentAuthor_username`) REFERENCES `profile` (`username`),
  ADD CONSTRAINT `FKbj0foibffv161na6i7iobe3yp` FOREIGN KEY (`post_content_id`) REFERENCES `post` (`content_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
