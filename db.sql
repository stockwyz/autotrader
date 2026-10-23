-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: autotrader
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cms_banner`
--

DROP TABLE IF EXISTS `cms_banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cms_banner` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `image_url` varchar(500) COLLATE utf8mb4_general_ci NOT NULL,
  `title` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `link_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sort_no` int DEFAULT '0',
  `status` tinyint DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='首页轮播图';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_banner`
--

LOCK TABLES `cms_banner` WRITE;
/*!40000 ALTER TABLE `cms_banner` DISABLE KEYS */;
INSERT INTO `cms_banner` VALUES (1,'/uploads/banner/img-1786242797678.jpg','TestBanner','https://www.msn.com/en-ca/news/other/judge-rules-homeless-man-s-tent-met-accommodation-requirements-for-enhanced-income-assistance/ar-AA29B4yr?ocid=msedgntp&pc=NMTS&cvid=6a768989dd1e4069a1d5dc5a91108692&ei=11',1,1,'2026-08-08 10:10:27'),(2,'/uploads/banner/img-1786242709435.jpg','TestBB2','',0,1,'2026-08-09 10:31:50');
/*!40000 ALTER TABLE `cms_banner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_career`
--

DROP TABLE IF EXISTS `cms_career`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cms_career` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `department` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `location` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `job_type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '全职/兼职',
  `description` text COLLATE utf8mb4_general_ci,
  `status` tinyint DEFAULT '1' COMMENT '1招聘中 0已关闭',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='招聘岗位(Careers)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_career`
--

LOCK TABLES `cms_career` WRITE;
/*!40000 ALTER TABLE `cms_career` DISABLE KEYS */;
INSERT INTO `cms_career` VALUES (1,'Web Operation Test','Operation Dept','Winnipeg','Full Time','Web Operation Duty:\nA\nB\nC\nD\nWeb Operation requirement:\n1\n2\n3\n4\n5\n6',1,'2026-08-09 07:51:27'),(2,'Sales','Sale Dept','Winnipeg','Full-time','Duty:\na\nb\nc\nd\nRequirement:\nX\nY\nZ',1,'2026-08-09 08:02:42');
/*!40000 ALTER TABLE `cms_career` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_nav_menu`
--

DROP TABLE IF EXISTS `cms_nav_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cms_nav_menu` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` bigint unsigned DEFAULT '0' COMMENT '0为一级菜单',
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `link_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sort_no` int DEFAULT '0',
  `status` tinyint DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='顶部导航菜单配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_nav_menu`
--

LOCK TABLES `cms_nav_menu` WRITE;
/*!40000 ALTER TABLE `cms_nav_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_nav_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_page`
--

DROP TABLE IF EXISTS `cms_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cms_page` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '页面标识 如 about-us/finance/services/detailing/privacy',
  `title` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '富文本HTML',
  `seo_title` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `seo_keywords` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `seo_description` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_page_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='静态/富文本页面内容';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_page`
--

LOCK TABLES `cms_page` WRITE;
/*!40000 ALTER TABLE `cms_page` DISABLE KEYS */;
INSERT INTO `cms_page` VALUES (1,'about-us','About Us','<p><span style=\"font-size: 18px; font-family: arial, helvetica, sans-serif;\">At BlackStone Auto, we put our customers first. We understand that buying a vehicle is a big decision, and we&rsquo;re committed to making the process easy, transparent, and stress-free.</span></p>\n<p><span style=\"font-size: 18px; font-family: arial, helvetica, sans-serif;\">We take pride in meeting&mdash;and exceeding&mdash;your expectations with quality vehicles, honest advice, and exceptional service every step of the way.</span></p>\n<p><span style=\"font-size: 18px; font-family: arial, helvetica, sans-serif;\">Visit us and experience the BlackStone Auto difference.</span></p>\n<p><span style=\"font-size: 18px;\"><img style=\"margin: 0px;\" src=\"../../uploads/page-content/img-1786164263816.jpg\" alt=\"\" width=\"580\" height=\"387\"> <img style=\"margin: 0px;\" src=\"../../uploads/page-content/img-1786164283109.jpg\" alt=\"\" width=\"580\" height=\"387\"></span><img style=\"margin: 0px;\" src=\"../../uploads/page-content/img-1786168105039.jpg\" alt=\"\" width=\"580\" height=\"386\"> <img style=\"margin-left: 0px; margin-right: 0px;\" src=\"../../uploads/page-content/img-1786168163710.jpg\" alt=\"\" width=\"580\" height=\"386\"></p>','','','','2026-08-08 12:29:28');
/*!40000 ALTER TABLE `cms_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_testimonial`
--

DROP TABLE IF EXISTS `cms_testimonial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cms_testimonial` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `rating` tinyint DEFAULT '5' COMMENT '1-5星',
  `content` text COLLATE utf8mb4_general_ci NOT NULL,
  `source` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '如 Google/Facebook',
  `status` tinyint DEFAULT '1',
  `sort_no` int DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='客户评价(Reviews/Testimonials)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_testimonial`
--

LOCK TABLES `cms_testimonial` WRITE;
/*!40000 ALTER TABLE `cms_testimonial` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_testimonial` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dealer_business_hours`
--

DROP TABLE IF EXISTS `dealer_business_hours`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dealer_business_hours` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `day_of_week` tinyint NOT NULL COMMENT '1=周一...7=周日',
  `open_time` time DEFAULT NULL COMMENT '为空表示当天休息',
  `close_time` time DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_day` (`day_of_week`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='营业时间(Our Hours)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dealer_business_hours`
--

LOCK TABLES `dealer_business_hours` WRITE;
/*!40000 ALTER TABLE `dealer_business_hours` DISABLE KEYS */;
INSERT INTO `dealer_business_hours` VALUES (1,1,'10:00:00','20:00:00'),(2,2,'07:00:00','19:30:00'),(3,3,'07:30:00','19:00:00'),(4,4,'10:00:00','18:30:00'),(5,5,'08:30:00','18:00:00'),(6,6,'09:00:00','17:30:00'),(7,7,NULL,NULL);
/*!40000 ALTER TABLE `dealer_business_hours` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dealer_info`
--

DROP TABLE IF EXISTS `dealer_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dealer_info` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'McWilliam Auto',
  `logo_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '门店Logo相对路径',
  `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `dealer_permit_no` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `facebook_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instagram_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `latitude` decimal(10,6) DEFAULT NULL,
  `longitude` decimal(10,6) DEFAULT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `website_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '经销商官网地址，展示在车辆卡片等位置',
  `theme_code` varchar(10) COLLATE utf8mb4_general_ci DEFAULT 'A' COMMENT '前台网站风格 A晴空蓝 B森林绿 C青蓝绿 D酒红',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='门店基础信息(单条记录)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dealer_info`
--

LOCK TABLES `dealer_info` WRITE;
/*!40000 ALTER TABLE `dealer_info` DISABLE KEYS */;
INSERT INTO `dealer_info` VALUES (1,'Black Stone','dealer/black-stone-1786300569044.png','3-27 South Landing Drive, Oak Bluff MB',' (204)219-0731','fjrocket@gmail.com',NULL,'https://www.facebook.com/','https://www.instagram.com/',NULL,NULL,'2026-10-13 05:28:51','http://blackstone.ca','D');
/*!40000 ALTER TABLE `dealer_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dict_body_style`
--

DROP TABLE IF EXISTS `dict_body_style`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dict_body_style` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '如 Sedan/SUV/Pickup/Van-Minivan',
  `photo_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '车身类型示例图片相对路径',
  `sort_no` int DEFAULT '0',
  `status` tinyint DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_body_style_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='车身类型字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dict_body_style`
--

LOCK TABLES `dict_body_style` WRITE;
/*!40000 ALTER TABLE `dict_body_style` DISABLE KEYS */;
INSERT INTO `dict_body_style` VALUES (1,'Sedan','bodystyle/logo-1785716683559.png',1,1),(2,'SUV','bodystyle/logo-1785717734052.png',2,1),(3,'Pickup','bodystyle/logo-1785717743536.png',3,1),(4,'Van-Minivan','bodystyle/logo-1785717752491.png',4,1);
/*!40000 ALTER TABLE `dict_body_style` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dict_color`
--

DROP TABLE IF EXISTS `dict_color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dict_color` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `hex_code` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '前端色块展示',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_color_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='颜色字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dict_color`
--

LOCK TABLES `dict_color` WRITE;
/*!40000 ALTER TABLE `dict_color` DISABLE KEYS */;
INSERT INTO `dict_color` VALUES (1,'Blue','#3E6FA8'),(2,'White','#FFFFFF'),(3,'Silver','#C0C0C0'),(4,'Black','#1A1A1A'),(5,'Red','#C0392B'),(6,'Gray','#808080'),(7,'Brown','#6B4423'),(8,'Grey','#808080'),(9,'Green','#2E7D32'),(10,'Dark Blue','#1B2A4A');
/*!40000 ALTER TABLE `dict_color` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dict_engine`
--

DROP TABLE IF EXISTS `dict_engine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dict_engine` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '如 4 Cylinder/6 Cylinder/8 Cylinder/Electric',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_engine_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='发动机字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dict_engine`
--

LOCK TABLES `dict_engine` WRITE;
/*!40000 ALTER TABLE `dict_engine` DISABLE KEYS */;
INSERT INTO `dict_engine` VALUES (11,'12 Cylinder'),(1,'4 Cylinder'),(2,'6 Cylinder'),(3,'8 Cylinder'),(4,'Electric');
/*!40000 ALTER TABLE `dict_engine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dict_feature`
--

DROP TABLE IF EXISTS `dict_feature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dict_feature` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '如 ABS Brakes / Sunroof/Moonroof / Leather Seats',
  `category` varchar(50) COLLATE utf8mb4_general_ci DEFAULT 'general' COMMENT '分组，便于前端展示',
  `sort_no` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_feature_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='通用配置字典(用于车辆勾选)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dict_feature`
--

LOCK TABLES `dict_feature` WRITE;
/*!40000 ALTER TABLE `dict_feature` DISABLE KEYS */;
/*!40000 ALTER TABLE `dict_feature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dict_fuel_type`
--

DROP TABLE IF EXISTS `dict_fuel_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dict_fuel_type` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Gasoline/Electric/Hybrid/Other',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_fuel_type_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='燃料类型字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dict_fuel_type`
--

LOCK TABLES `dict_fuel_type` WRITE;
/*!40000 ALTER TABLE `dict_fuel_type` DISABLE KEYS */;
INSERT INTO `dict_fuel_type` VALUES (2,'Electric'),(1,'Gasoline'),(3,'Hybrid'),(4,'Other');
/*!40000 ALTER TABLE `dict_fuel_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dict_make`
--

DROP TABLE IF EXISTS `dict_make`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dict_make` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '品牌名 如 Toyota',
  `logo_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '品牌Logo图标URL/路径',
  `sort_no` int DEFAULT '0',
  `status` tinyint DEFAULT '1' COMMENT '1启用 0禁用',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_make_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='车辆品牌字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dict_make`
--

LOCK TABLES `dict_make` WRITE;
/*!40000 ALTER TABLE `dict_make` DISABLE KEYS */;
INSERT INTO `dict_make` VALUES (1,'Audi','make/logo-1785701063472.png',1,1,'2026-08-02 10:08:57'),(2,'BMW','make/bmw.png',2,1,'2026-08-02 10:08:57'),(3,'Cadillac','make/cadillac.png',3,1,'2026-08-02 10:08:57'),(4,'Chevrolet','make/chevrolet.png',4,1,'2026-08-02 10:08:57'),(5,'Chrysler','make/chrysler.png',5,1,'2026-08-02 10:08:57'),(6,'Dodge','make/dodge.png',6,1,'2026-08-02 10:08:57'),(7,'Ford','make/ford.png',7,1,'2026-08-02 10:08:57'),(8,'Honda','make/honda.png',8,1,'2026-08-02 10:08:57'),(9,'Hyundai','make/hyundai.png',9,1,'2026-08-02 10:08:57'),(10,'Infiniti','make/infiniti.png',10,1,'2026-08-02 10:08:57'),(11,'Jeep','make/jeep.png',11,1,'2026-08-02 10:08:57'),(12,'Kia','make/kia.png',12,1,'2026-08-02 10:08:57'),(13,'Land Rover','make/land-rover.png',13,1,'2026-08-02 10:08:57'),(14,'Lexus','make/lexus.png',14,1,'2026-08-02 10:08:57'),(15,'Mazda','make/mazda.png',15,1,'2026-08-02 10:08:57'),(16,'Mercedes-Benz','make/mercedes-benz.png',16,1,'2026-08-02 10:08:57'),(17,'Mitsubishi','make/mitsubishi.png',17,1,'2026-08-02 10:08:57'),(18,'Nissan','make/nissan.png',18,1,'2026-08-02 10:08:57'),(19,'Porsche','make/porsche.png',19,1,'2026-08-02 10:08:57'),(20,'Ram','make/ram.png',20,1,'2026-08-02 10:08:57'),(21,'Tahoe','make/logo-1789230950348.png',21,1,'2026-08-02 10:08:57'),(22,'Tesla','make/tesla.png',22,1,'2026-08-02 10:08:57'),(23,'Toyota','make/toyota.png',23,1,'2026-08-02 10:08:57'),(31,'TestMake','make/logo-1785733124820.png',23,1,'2026-08-03 12:58:44'),(34,'Tesla1','make/logo-1789185966167.png',24,1,'2026-09-12 12:06:06');
/*!40000 ALTER TABLE `dict_make` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dict_model`
--

DROP TABLE IF EXISTS `dict_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dict_model` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `make_id` bigint unsigned NOT NULL COMMENT '所属品牌',
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '车型名 如 Highlander',
  `sort_no` int DEFAULT '0',
  `status` tinyint DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_make_id` (`make_id`),
  CONSTRAINT `fk_model_make` FOREIGN KEY (`make_id`) REFERENCES `dict_make` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='车型字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dict_model`
--

LOCK TABLES `dict_model` WRITE;
/*!40000 ALTER TABLE `dict_model` DISABLE KEYS */;
INSERT INTO `dict_model` VALUES (1,1,'A4',1,1,'2026-08-02 10:08:57'),(2,1,'A6',2,1,'2026-08-02 10:08:57'),(3,1,'Q5',3,1,'2026-08-02 10:08:57'),(4,1,'Q7',4,1,'2026-08-02 10:08:57'),(5,2,'3 Series',1,1,'2026-08-02 10:08:57'),(6,2,'5 Series',2,1,'2026-08-02 10:08:57'),(7,2,'X3',3,1,'2026-08-02 10:08:57'),(8,2,'X5',4,1,'2026-08-02 10:08:57'),(9,3,'Escalade',1,1,'2026-08-02 10:08:57'),(10,3,'CTS',2,1,'2026-08-02 10:08:57'),(11,3,'XT5',3,1,'2026-08-02 10:08:57'),(12,4,'Silverado 1500',1,1,'2026-08-02 10:08:57'),(13,4,'Equinox',2,1,'2026-08-02 10:08:57'),(14,4,'Tahoe',3,1,'2026-08-02 10:08:57'),(15,4,'Malibu',4,1,'2026-08-02 10:08:57'),(16,4,'Traverse',5,1,'2026-08-02 10:08:57'),(17,5,'300',1,1,'2026-08-02 10:08:57'),(18,5,'Pacifica',2,1,'2026-08-02 10:08:57'),(19,6,'Charger',1,1,'2026-08-02 10:08:57'),(20,6,'Durango',2,1,'2026-08-02 10:08:57'),(21,6,'Grand Caravan',3,1,'2026-08-02 10:08:57'),(22,6,'Journey',4,1,'2026-08-02 10:08:57'),(23,7,'F-150',1,1,'2026-08-02 10:08:57'),(24,7,'Escape',2,1,'2026-08-02 10:08:57'),(25,7,'Explorer',3,1,'2026-08-02 10:08:57'),(26,7,'Edge',4,1,'2026-08-02 10:08:57'),(27,7,'Fusion',5,1,'2026-08-02 10:08:57'),(28,8,'Civic',1,1,'2026-08-02 10:08:57'),(29,8,'Accord',2,1,'2026-08-02 10:08:57'),(30,8,'CR-V',3,1,'2026-08-02 10:08:57'),(31,8,'Pilot',4,1,'2026-08-02 10:08:57'),(32,9,'Elantra',1,1,'2026-08-02 10:08:57'),(33,9,'Tucson',2,1,'2026-08-02 10:08:57'),(34,9,'Santa Fe',3,1,'2026-08-02 10:08:57'),(35,9,'Sonata',4,1,'2026-08-02 10:08:57'),(36,10,'Q50',1,1,'2026-08-02 10:08:57'),(37,10,'QX60',2,1,'2026-08-02 10:08:57'),(38,11,'Grand Cherokee',1,1,'2026-08-02 10:08:57'),(39,11,'Wrangler',2,1,'2026-08-02 10:08:57'),(40,11,'Cherokee',3,1,'2026-08-02 10:08:57'),(41,11,'Compass',4,1,'2026-08-02 10:08:57'),(42,12,'Sportage',1,1,'2026-08-02 10:08:57'),(43,12,'Sorento',2,1,'2026-08-02 10:08:57'),(44,12,'Forte',3,1,'2026-08-02 10:08:57'),(45,12,'Soul',4,1,'2026-08-02 10:08:57'),(46,13,'Range Rover',1,1,'2026-08-02 10:08:57'),(47,13,'Discovery',2,1,'2026-08-02 10:08:57'),(48,13,'Range Rover Evoque',3,1,'2026-08-02 10:08:57'),(49,14,'RX',1,1,'2026-08-02 10:08:57'),(50,14,'ES',2,1,'2026-08-02 10:08:57'),(51,14,'NX',3,1,'2026-08-02 10:08:57'),(52,15,'CX-5',1,1,'2026-08-02 10:08:57'),(53,15,'Mazda3',2,1,'2026-08-02 10:08:57'),(54,15,'CX-9',3,1,'2026-08-02 10:08:57'),(55,16,'C-Class',1,1,'2026-08-02 10:08:57'),(56,16,'E-Class',2,1,'2026-08-02 10:08:57'),(57,16,'GLC',3,1,'2026-08-02 10:08:57'),(58,16,'GLE',4,1,'2026-08-02 10:08:57'),(59,17,'Outlander',1,1,'2026-08-02 10:08:57'),(60,17,'RVR',2,1,'2026-08-02 10:08:57'),(61,18,'Rogue',1,1,'2026-08-02 10:08:57'),(62,18,'Altima',2,1,'2026-08-02 10:08:57'),(63,18,'Murano',3,1,'2026-08-02 10:08:57'),(64,18,'Sentra',4,1,'2026-08-02 10:08:57'),(65,18,'Pathfinder',5,1,'2026-08-02 10:08:57'),(66,19,'Cayenne',1,1,'2026-08-02 10:08:57'),(67,19,'Macan',2,1,'2026-08-02 10:08:57'),(68,20,'1500',1,1,'2026-08-02 10:08:57'),(69,20,'2500',2,1,'2026-08-02 10:08:57'),(70,20,'ProMaster',3,1,'2026-08-02 10:08:57'),(71,21,'Tahoe',1,1,'2026-08-02 10:08:57'),(72,22,'Model 3',1,1,'2026-08-02 10:08:57'),(73,22,'Model Y',2,1,'2026-08-02 10:08:57'),(74,22,'Model S',3,1,'2026-08-02 10:08:57'),(75,23,'RAV4',1,1,'2026-08-02 10:08:57'),(76,23,'Camry',2,1,'2026-08-02 10:08:57'),(77,23,'Highlander',3,1,'2026-08-02 10:08:57'),(78,23,'Corolla',4,1,'2026-08-02 10:08:57'),(79,23,'Tacoma',5,1,'2026-08-02 10:08:57'),(80,1,'A8',2,1,'2026-08-03 05:23:06'),(82,31,'TestAudi',1,1,'2026-08-03 12:58:21'),(83,1,'Q8',5,1,'2026-09-12 12:10:07');
/*!40000 ALTER TABLE `dict_model` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dict_transmission`
--

DROP TABLE IF EXISTS `dict_transmission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dict_transmission` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '如 Automatic/CVT/8 Speed Automatic',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_transmission_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='变速箱字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dict_transmission`
--

LOCK TABLES `dict_transmission` WRITE;
/*!40000 ALTER TABLE `dict_transmission` DISABLE KEYS */;
INSERT INTO `dict_transmission` VALUES (3,'8 Speed Automatic'),(1,'Automatic'),(2,'CVT'),(9,'TestTransmission1');
/*!40000 ALTER TABLE `dict_transmission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finance_setting`
--

DROP TABLE IF EXISTS `finance_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `finance_setting` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `default_interest_rate` decimal(5,2) NOT NULL DEFAULT '7.99' COMMENT '默认年利率%',
  `min_term_months` smallint DEFAULT '24',
  `max_term_months` smallint DEFAULT '96',
  `default_term_months` smallint DEFAULT '72',
  `min_down_payment_pct` decimal(5,2) DEFAULT '0.00',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='贷款计算器/月供估算默认参数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finance_setting`
--

LOCK TABLES `finance_setting` WRITE;
/*!40000 ALTER TABLE `finance_setting` DISABLE KEYS */;
INSERT INTO `finance_setting` VALUES (1,7.99,24,96,72,0.00,'2026-08-02 06:02:13');
/*!40000 ALTER TABLE `finance_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lead_car_finder`
--

DROP TABLE IF EXISTS `lead_car_finder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lead_car_finder` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `desired_make` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `desired_model` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `min_year` smallint DEFAULT NULL,
  `max_year` smallint DEFAULT NULL,
  `budget_min` decimal(10,2) DEFAULT NULL,
  `budget_max` decimal(10,2) DEFAULT NULL,
  `message` text COLLATE utf8mb4_general_ci,
  `status` tinyint DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `desired_trim` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '期望车型配置',
  `desired_km` int DEFAULT NULL COMMENT '期望公里数上限',
  `desired_body_style` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '期望车身类型',
  `desired_transmission` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '期望变速箱',
  `desired_driveline` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '期望驱动方式 如 FWD/RWD/AWD/4WD',
  `desired_fuel_type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '期望燃油类型',
  `desired_color` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '期望颜色',
  `desired_condition` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '期望车况 Excellent/Good/Fair/Poor',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Car Finder 选车顾问需求';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lead_car_finder`
--

LOCK TABLES `lead_car_finder` WRITE;
/*!40000 ALTER TABLE `lead_car_finder` DISABLE KEYS */;
INSERT INTO `lead_car_finder` VALUES (1,'Haitao','Zhang','fjrocsdsd@gmail.com','2046323212','Cadillac','CTS',NULL,NULL,NULL,232332.00,'',0,'2026-08-09 04:46:01','',NULL,'','','','','','');
/*!40000 ALTER TABLE `lead_car_finder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lead_carfax_request`
--

DROP TABLE IF EXISTS `lead_carfax_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lead_carfax_request` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `vehicle_id` bigint unsigned NOT NULL,
  `first_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_vehicle_id` (`vehicle_id`),
  CONSTRAINT `fk_carfax_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='CARFAX报告请求记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lead_carfax_request`
--

LOCK TABLES `lead_carfax_request` WRITE;
/*!40000 ALTER TABLE `lead_carfax_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `lead_carfax_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lead_contact`
--

DROP TABLE IF EXISTS `lead_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lead_contact` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'CONTACT' COMMENT 'CONTACT/TEXT_US',
  `first_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `message` text COLLATE utf8mb4_general_ci,
  `status` tinyint DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='联系我们/短信联系(Contact Us / Text Us Now)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lead_contact`
--

LOCK TABLES `lead_contact` WRITE;
/*!40000 ALTER TABLE `lead_contact` DISABLE KEYS */;
INSERT INTO `lead_contact` VALUES (3,'CONTACT','Haitao','Zhang','fjrocket00@gmail.com','2046197940','',0,'2026-08-05 14:28:11'),(7,'CONTACT','hai','Zhang','fjrocket00@gmail.com','2046197940','sdfsdfsdf',0,'2026-08-08 10:36:00'),(8,'CONTACT','232323','Zhang','fjrocket00@gmail.com','','232046197940',0,'2026-08-08 10:40:13'),(10,'CONTACT','sdsd','z','zht517826817@gmail.com','','hgfgfg',0,'2026-08-08 11:57:27');
/*!40000 ALTER TABLE `lead_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lead_inquiry`
--

DROP TABLE IF EXISTS `lead_inquiry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lead_inquiry` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `vehicle_id` bigint unsigned DEFAULT NULL COMMENT '关联车辆,可为空(如非车辆页面咨询)',
  `first_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `message` text COLLATE utf8mb4_general_ci,
  `source_page` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '来源页面',
  `status` tinyint DEFAULT '0' COMMENT '0待处理 1跟进中 2已完成',
  `assigned_to` bigint unsigned DEFAULT NULL COMMENT '分配给的销售顾问(sys_user.id)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_vehicle_id` (`vehicle_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='询价留资(Get More Information)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lead_inquiry`
--

LOCK TABLES `lead_inquiry` WRITE;
/*!40000 ALTER TABLE `lead_inquiry` DISABLE KEYS */;
INSERT INTO `lead_inquiry` VALUES (2,4,'jrocket','z','zht517826817@gmail.com','13910038957','','vehicle-detail',0,NULL,'2026-08-05 14:31:42'),(3,8,'Cheap','Cheap','sdfdsf@sdfs','2046197912','2123123123123','vehicle-detail',0,NULL,'2026-08-09 10:11:42'),(4,6,'Cheap','Cheap','sdfdsf@sdfs','2046197940','sdfsadfdsafdsfdsa','vehicle-detail',0,NULL,'2026-09-12 12:30:40'),(5,6,'Cheap','Cheap','sdfdsf@sdfs','2046197940','','vehicle-detail',0,NULL,'2026-09-12 12:31:33');
/*!40000 ALTER TABLE `lead_inquiry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lead_loan_application`
--

DROP TABLE IF EXISTS `lead_loan_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lead_loan_application` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `vehicle_id` bigint unsigned DEFAULT NULL COMMENT '意向车辆,可为空',
  `first_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `address` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `city` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `province` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `postal_code` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `years_at_address` decimal(4,1) DEFAULT NULL,
  `employer_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `job_title` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `employment_years` decimal(4,1) DEFAULT NULL,
  `monthly_income_enc` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '月收入(加密存储)',
  `down_payment` decimal(10,2) DEFAULT NULL,
  `message` text COLLATE utf8mb4_general_ci,
  `status` tinyint DEFAULT '0' COMMENT '0待处理 1审核中 2已通过 3已拒绝',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `salutation` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '称谓',
  `gender` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '性别',
  `marital_status` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '婚姻状况',
  `sin_enc` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '社会保险号(SIN)，生产环境应加密存储',
  `address_duration_years` int DEFAULT NULL COMMENT '现住址居住年数',
  `address_duration_months` int DEFAULT NULL COMMENT '现住址居住月数',
  `home_status` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '住房状态 Rent/Own等',
  `monthly_housing_payment` decimal(10,2) DEFAULT NULL COMMENT '每月房租/房贷金额',
  `employment_type` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '雇佣类型',
  `employment_address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工作单位地址',
  `employment_city` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工作单位所在城市',
  `employment_province` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工作单位所在省份',
  `employment_postal_code` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工作单位邮编',
  `employment_phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工作单位电话',
  `employment_duration_years` int DEFAULT NULL COMMENT '当前工作年数',
  `employment_duration_months` int DEFAULT NULL COMMENT '当前工作月数',
  `previous_employer` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上一份工作雇主',
  `previous_employer_phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上一份工作雇主电话',
  `previous_employment_duration_years` int DEFAULT NULL COMMENT '上一份工作年数',
  `previous_employment_duration_months` int DEFAULT NULL COMMENT '上一份工作月数',
  `previous_bankruptcy` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否曾破产 Yes/No',
  `previous_repossession` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否曾被收回车辆 Yes/No',
  `cosigner_available` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否有共同签署人 Yes/No',
  `credit_rating` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '自评信用等级',
  `desired_make` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '意向车辆品牌',
  `desired_model` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '意向车辆车型',
  `desired_year` int DEFAULT NULL COMMENT '意向车辆年份',
  `desired_vin` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '意向车辆VIN',
  `trade_make` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '旧车品牌',
  `trade_model` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '旧车车型',
  `trade_year` int DEFAULT NULL COMMENT '旧车年份',
  `trade_km` int DEFAULT NULL COMMENT '旧车公里数',
  PRIMARY KEY (`id`),
  KEY `idx_vehicle_id` (`vehicle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='信贷申请(Apply For Credit)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lead_loan_application`
--

LOCK TABLES `lead_loan_application` WRITE;
/*!40000 ALTER TABLE `lead_loan_application` DISABLE KEYS */;
INSERT INTO `lead_loan_application` VALUES (1,NULL,'First Name','Last Name','fjrocket23@gmail.com','2046194532','2026-08-03','Address','Winnipeg','Manitoba','R3Y ABC',NULL,'Employer','Occupation',NULL,'123123',NULL,'',0,'2026-08-09 07:50:38','','','Married',NULL,1,1,'Own',1223.00,'Full-Time','Employment Address','Winnipeg','Manitoba','R3Y ABC','2046237923',1,2,'','',NULL,NULL,'','','','','','',NULL,'','','',NULL,NULL),(2,NULL,'H','T','fjrocket00@gmail.com','2046197940','2026-09-09','Unit 408, 360 Bridge Lake Dr, Winnipeg, MB R3Y2B1','Winnipeg','Manitoba','R3Y 2B1',NULL,'BlackStone','Software developer',NULL,'64000',NULL,'',0,'2026-09-12 12:26:30','Mr.','Male','Married',NULL,12,11,'Rent',1232121.00,'Full-Time','Unit 408, 360 Bridge Lake Dr, Winnipeg, MB R3Y2B1','Winnipeg','Manitoba','R3Y 2B1','2046197940',12,11,'','',NULL,NULL,'','','','','','',NULL,'','','',NULL,NULL);
/*!40000 ALTER TABLE `lead_loan_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lead_test_drive`
--

DROP TABLE IF EXISTS `lead_test_drive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lead_test_drive` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `vehicle_id` bigint unsigned DEFAULT NULL,
  `first_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `message` text COLLATE utf8mb4_general_ci,
  `preferred_time` datetime DEFAULT NULL COMMENT '期望试驾时间',
  `status` tinyint DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_vehicle_id` (`vehicle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='试驾预约(Test Drive This Vehicle)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lead_test_drive`
--

LOCK TABLES `lead_test_drive` WRITE;
/*!40000 ALTER TABLE `lead_test_drive` DISABLE KEYS */;
INSERT INTO `lead_test_drive` VALUES (1,4,'Haitao','Zhang','fjrocket00@gmail.com','2046197940','',NULL,0,'2026-08-05 14:25:09'),(2,4,'Haitao','Zhang','fjrocket00@gmail.com','2046197940','',NULL,0,'2026-08-05 14:25:13'),(3,4,'Haitao','Zhang','fjrocket00@gmail.com','dfssadfsadf','',NULL,0,'2026-08-05 14:25:30'),(4,8,'Test','Test','fjrockdsfsd@gmail.com','2046197323','tewetr','2026-08-19 08:01:01',0,'2026-08-09 10:12:28');
/*!40000 ALTER TABLE `lead_test_drive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lead_trade_in`
--

DROP TABLE IF EXISTS `lead_trade_in`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lead_trade_in` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `trade_year` smallint DEFAULT NULL COMMENT '旧车年款',
  `trade_make` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `trade_model` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `trade_trim` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `trade_km` int DEFAULT NULL COMMENT '旧车公里数',
  `trade_vin` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `condition_note` text COLLATE utf8mb4_general_ci COMMENT '车况描述',
  `message` text COLLATE utf8mb4_general_ci,
  `status` tinyint DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `trade_body_style` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '旧车车身类型',
  `trade_transmission` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '旧车变速箱',
  `trade_driveline` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '旧车驱动方式 如 FWD/RWD/AWD/4WD',
  `trade_fuel_type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '旧车燃油类型',
  `trade_color` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '旧车颜色',
  `trade_condition` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '旧车车况 Excellent/Good/Fair/Poor',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='以旧换新估价申请(Trade-In Appraisal)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lead_trade_in`
--

LOCK TABLES `lead_trade_in` WRITE;
/*!40000 ALTER TABLE `lead_trade_in` DISABLE KEYS */;
INSERT INTO `lead_trade_in` VALUES (1,'ff','ddd','esdfsad@gmail.com','2046197940',NULL,'Audi','A6','',NULL,'','',NULL,0,'2026-08-09 02:25:17','','','','','',''),(2,'ff','ddd','esdfsad@gmail.com','2046197940',2025,'Audi','A6','3',123456,'321','Additional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional InformationAdditional Information',NULL,0,'2026-08-09 02:26:19','Sedan','8 Speed Automatic','4WD','Electric','Red','Good'),(3,'SADF','K','abct00@gmail.com','2046327642',2025,'Audi','A6','',NULL,'','',NULL,0,'2026-08-09 04:29:11','','','','','','');
/*!40000 ALTER TABLE `lead_trade_in` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pro_label`
--

DROP TABLE IF EXISTS `pro_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pro_label` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '标签名称',
  `image_url` varchar(255) DEFAULT NULL COMMENT '标签图片相对路径，如 label/img-xxx.png',
  `width_percent` int DEFAULT '100' COMMENT '宽度缩放百分比，100=基准宽度，支持跟height_percent独立拉伸',
  `height_percent` int DEFAULT '100' COMMENT '高度缩放百分比，100=基准高度，支持跟width_percent独立拉伸',
  `angle` int DEFAULT '0' COMMENT '旋转角度，0为不旋转，顺时针为正',
  `pos_x` decimal(5,2) DEFAULT '50.00' COMMENT '标签中心点X坐标，相对图片容器宽度的百分比 0-100',
  `pos_y` decimal(5,2) DEFAULT '50.00' COMMENT '标签中心点Y坐标，相对图片容器高度的百分比 0-100',
  `sort_no` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '1=启用 0=停用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='促销标签(Pro-Label)设计';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pro_label`
--

LOCK TABLES `pro_label` WRITE;
/*!40000 ALTER TABLE `pro_label` DISABLE KEYS */;
INSERT INTO `pro_label` VALUES (1,'sold','/uploads/label/img-1786293944591.png',311,270,344,49.00,57.10,0,1),(2,'featured','/uploads/label/img-1786294396847.png',102,108,0,10.10,8.30,0,1),(3,'oneowner','/uploads/label/img-1786294811508.png',84,87,0,8.80,27.80,0,1),(4,'lowkm','/uploads/label/img-1786294869371.png',79,88,0,8.80,44.70,0,1);
/*!40000 ALTER TABLE `pro_label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_appointment`
--

DROP TABLE IF EXISTS `service_appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_appointment` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `service_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'REPAIR / MAINTENANCE / PHR',
  `appt_date` date NOT NULL COMMENT '预约日期',
  `appt_time` time NOT NULL COMMENT '预约时间段起点',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '客户填写的详细说明',
  `customer_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `vehicle_info` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '年款/品牌/车型(选填)',
  `status` tinyint DEFAULT '0' COMMENT '0=新预约 1=已确认 2=已完成 3=已取消(取消后释放时间段)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `confirm_token` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮件里确认/取消按钮用的随机令牌',
  `customer_response` tinyint DEFAULT '0' COMMENT '客户在邮件里的操作：0=未操作 1=客户点了确认 2=客户点了取消',
  `responded_at` datetime DEFAULT NULL COMMENT '客户点击确认/取消的时间',
  `appt_no` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预约单号，如 AP-20261013-7K3Q',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_appt_no` (`appt_no`),
  KEY `idx_appt_date_time` (`appt_date`,`appt_time`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='维修/养护预约(Appointment)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_appointment`
--

LOCK TABLES `service_appointment` WRITE;
/*!40000 ALTER TABLE `service_appointment` DISABLE KEYS */;
INSERT INTO `service_appointment` VALUES (1,'REPAIR','2026-10-15','14:30:00','Test appointment','Frank Zhang','4316197940','swwan03@gmail.com','2026, audi, a5',3,'2026-10-12 14:47:57',NULL,0,NULL,'AP-20261012-0001'),(2,'MAINTENANCE','2026-10-13','07:00:00','test appointment','Haitao Zhang','2046197940','swwan03@gmail.com','2026, audi, a5',0,'2026-10-13 02:57:21','33ddda4b08dd4faf8e6ae0961853dee8',0,NULL,'AP-20261013-0002'),(3,'REPAIR','2026-10-13','07:30:00',NULL,'Haitao Zhang','2046197940',NULL,'2026, audi, a5',0,'2026-10-13 03:01:28','34dc82b28df7402cb339a98516a6324e',0,NULL,'AP-20261013-0003');
/*!40000 ALTER TABLE `service_appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_operation_log`
--

DROP TABLE IF EXISTS `sys_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_operation_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned DEFAULT NULL,
  `module` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `action` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `content` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ip` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='后台操作日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_operation_log`
--

LOCK TABLES `sys_operation_log` WRITE;
/*!40000 ALTER TABLE `sys_operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_permission` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `perm_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `perm_code` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '如 vehicle:edit',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_perm_code` (`perm_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限点';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '如 超级管理员/销售顾问/内容编辑',
  `role_code` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_permission`
--

DROP TABLE IF EXISTS `sys_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_permission` (
  `role_id` bigint unsigned NOT NULL,
  `permission_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色-权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_permission`
--

LOCK TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `real_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `avatar_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` tinyint DEFAULT '1' COMMENT '1启用 0禁用',
  `last_login_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='后台管理用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','$2b$10$y4FqjQ7UBNkk9lKzEhP56OQRUNmwif5.O1FNdID2FiDYMY50NpR6a','系统管理员',NULL,NULL,NULL,1,'2026-10-12 12:45:56','2026-08-02 06:02:13');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint unsigned NOT NULL,
  `role_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户-角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `stock_no` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT '库存编号 Stock Number',
  `vin` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT '车架号',
  `title` varchar(150) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题 如 2022 Ram 1500 Limited 4x4',
  `slug` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL别名 如 2022-Ram-1500-Truck-Crew-Cab-703202',
  `make_id` bigint unsigned NOT NULL,
  `model_id` bigint unsigned NOT NULL,
  `year` smallint NOT NULL COMMENT '年款',
  `body_style_id` bigint unsigned NOT NULL,
  `transmission_id` bigint unsigned NOT NULL,
  `engine_id` bigint unsigned NOT NULL,
  `engine_size` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '排量 如 5.7 l',
  `fuel_type_id` bigint unsigned NOT NULL,
  `driveline` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '驱动方式 如 4X4/FWD/AWD',
  `exterior_color_id` bigint unsigned DEFAULT NULL,
  `interior_color_id` bigint unsigned DEFAULT NULL,
  `doors` tinyint DEFAULT NULL COMMENT '车门数',
  `passengers` tinyint DEFAULT NULL COMMENT '载客数',
  `odometer_km` int NOT NULL DEFAULT '0' COMMENT '公里数',
  `city_fuel_consumption` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '市区油耗 如 13.8L/100Km',
  `hwy_fuel_consumption` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '高速油耗',
  `price` decimal(10,2) NOT NULL COMMENT '售价',
  `biweekly_payment` decimal(10,2) DEFAULT NULL COMMENT '双周供估算(可后端计算实时值,此为快照/默认展示值)',
  `loan_term_months` smallint DEFAULT NULL COMMENT '月供估算对应期限，如72',
  `description` text COLLATE utf8mb4_general_ci COMMENT '车辆描述富文本(含厂商免责声明等)',
  `is_one_owner` tinyint(1) DEFAULT '0' COMMENT 'One Owner 徽章',
  `is_low_km` tinyint(1) DEFAULT '0' COMMENT 'Low KM 徽章',
  `is_featured` tinyint(1) DEFAULT '0' COMMENT '是否特价/精选车辆',
  `is_sold` tinyint(1) DEFAULT '0' COMMENT '是否已售',
  `status` tinyint DEFAULT '1' COMMENT '1上架 0下架',
  `carfax_pdf_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'CARFAX报告PDF地址',
  `video_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '车辆视频相对路径',
  `dealer_permit_no` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '经销商牌照号',
  `view_count` int DEFAULT '0' COMMENT '浏览次数',
  `sort_no` int DEFAULT '0' COMMENT '首页/列表排序权重',
  `created_by` bigint unsigned DEFAULT NULL,
  `updated_by` bigint unsigned DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sold_at` datetime DEFAULT NULL COMMENT '售出时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vehicle_stock_no` (`stock_no`),
  UNIQUE KEY `uk_vehicle_vin` (`vin`),
  UNIQUE KEY `uk_vehicle_slug` (`slug`),
  KEY `idx_make_model` (`make_id`,`model_id`),
  KEY `idx_year` (`year`),
  KEY `idx_price` (`price`),
  KEY `idx_odometer` (`odometer_km`),
  KEY `idx_body_style` (`body_style_id`),
  KEY `idx_status_sold_featured` (`status`,`is_sold`,`is_featured`),
  KEY `fk_vehicle_model` (`model_id`),
  KEY `fk_vehicle_transmission` (`transmission_id`),
  KEY `fk_vehicle_engine` (`engine_id`),
  KEY `fk_vehicle_fuel_type` (`fuel_type_id`),
  CONSTRAINT `fk_vehicle_body_style` FOREIGN KEY (`body_style_id`) REFERENCES `dict_body_style` (`id`),
  CONSTRAINT `fk_vehicle_engine` FOREIGN KEY (`engine_id`) REFERENCES `dict_engine` (`id`),
  CONSTRAINT `fk_vehicle_fuel_type` FOREIGN KEY (`fuel_type_id`) REFERENCES `dict_fuel_type` (`id`),
  CONSTRAINT `fk_vehicle_make` FOREIGN KEY (`make_id`) REFERENCES `dict_make` (`id`),
  CONSTRAINT `fk_vehicle_model` FOREIGN KEY (`model_id`) REFERENCES `dict_model` (`id`),
  CONSTRAINT `fk_vehicle_transmission` FOREIGN KEY (`transmission_id`) REFERENCES `dict_transmission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='二手车库存主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES (1,'202600001','2323','232323','2026-232323-514260',1,1,2026,2,3,1,'12',1,'',NULL,NULL,4,5,890,'','',0.00,NULL,72,'',0,0,1,0,1,NULL,'','',17,0,NULL,NULL,'2026-08-03 10:35:14','2026-09-30 11:21:24',NULL,0),(3,'202600002','232332323','TTSS','2026-TTSS-764002',2,6,2026,1,1,2,'',1,'',NULL,NULL,4,5,678,'','',1001.00,NULL,72,'',0,0,0,1,1,NULL,'','',33,0,NULL,NULL,'2026-08-04 02:46:04','2026-10-12 07:25:22',NULL,0),(4,'202600003','234324','titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle','2026-titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle-870264',3,11,2026,1,1,2,'5.7L',1,'4X4',2,1,5,4,456,'13.8','10.7',1000.00,NULL,72,'DESCDSD',0,0,1,0,1,NULL,'/uploads/202600003/video-a2e132fb1e1e4230b6d056192d3dc72e.mp4','',68,0,NULL,NULL,'2026-08-05 09:04:30','2026-10-12 07:46:11',NULL,0),(5,'202600004','dsf2r32','TEWERWER','2026-TEWERWER-807690',2,5,2022,1,3,2,'',2,'',NULL,NULL,4,5,0,'','',0.00,NULL,72,'Hiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw DescriptonsdfHiw Descriptonsdf',1,1,1,1,1,NULL,'','',55,0,NULL,NULL,'2026-08-05 12:06:48','2026-10-23 12:58:24',NULL,0),(6,'202600005','232432432','2022 Ram','2022-2022-Ram-948372',20,69,2022,3,1,3,'6.3L',1,'',2,3,4,5,1000,'13.8L/100KM','10L/100KM',5000.00,NULL,72,'Description',0,1,0,0,1,'/uploads/202600005/carfax.pdf','/uploads/202600005/video-1dc047bef01b43cbb39635550c831970.mp4','',20,0,NULL,NULL,'2026-08-09 08:55:48','2026-10-12 07:24:58',NULL,0),(7,'202600006','231234','sdfsdfsdfsdf','2026-sdfsdfsdfsdf-767372',1,80,2026,2,1,2,'',1,'',NULL,NULL,4,5,0,'','',0.00,NULL,72,'',1,0,1,0,1,'/uploads/202600006/carfax.pdf','/uploads/202600006/video-a9c38507110b485e882217edb431ea85.mp4','',38,0,NULL,NULL,'2026-08-09 09:09:27','2026-10-12 07:24:51',NULL,0),(8,'202600007','123123123','Ford 1500','2025-Ford-1500-442873',7,23,2025,3,3,3,'6.3L',1,'4*4',6,6,4,5,654321,'10L/km','15L/km',6000.00,NULL,72,'Description',1,1,0,0,1,'/uploads/202600007/carfax.pdf','/uploads/202600007/video-98c29f78e2f846b2a139d446cc254a6f.mp4','',24,0,NULL,NULL,'2026-08-09 10:10:43','2026-10-12 07:53:53',NULL,0),(9,'202600008','1232123','Nissian pathfinder 2021','2021-Nissian-pathfinder-2021-616763',18,65,2021,2,3,3,'12L',2,'AWD',5,4,4,5,0,'','',0.00,NULL,72,'',1,0,0,0,1,'','/uploads/202600008/video-07461d8549d542e6a235d562a9365dd6.mp4','',14,0,NULL,NULL,'2026-08-10 02:53:36','2026-10-12 07:25:30',NULL,0);
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_feature`
--

DROP TABLE IF EXISTS `vehicle_feature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_feature` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `vehicle_id` bigint unsigned NOT NULL,
  `category` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类: MECHANICAL/SAFETY/TECHNOLOGY/COMFORT/EXTERIOR/OFFROAD',
  `content` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '如 Adaptive Cruise Control (ACC)',
  `sort_no` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_vehicle_category` (`vehicle_id`,`category`),
  CONSTRAINT `fk_feature_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='车辆分类配置(详情页按类目分组展示)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_feature`
--

LOCK TABLES `vehicle_feature` WRITE;
/*!40000 ALTER TABLE `vehicle_feature` DISABLE KEYS */;
INSERT INTO `vehicle_feature` VALUES (28,8,'MECHANICAL','sdf',0),(29,6,'MECHANICAL','Cruise',0),(30,6,'SAFETY','6 airbags',1),(31,4,'COMFORT','SSD',0),(32,4,'MECHANICAL','MMMMM',1),(33,4,'SAFETY','Sdfwerwe',2),(36,9,'MECHANICAL','Nissian',0),(37,9,'SAFETY','Full bags',1);
/*!40000 ALTER TABLE `vehicle_feature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_feature_dict_rel`
--

DROP TABLE IF EXISTS `vehicle_feature_dict_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_feature_dict_rel` (
  `vehicle_id` bigint unsigned NOT NULL,
  `feature_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`vehicle_id`,`feature_id`),
  KEY `fk_vfd_feature` (`feature_id`),
  CONSTRAINT `fk_vfd_feature` FOREIGN KEY (`feature_id`) REFERENCES `dict_feature` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_vfd_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='车辆与通用配置字典多对多关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_feature_dict_rel`
--

LOCK TABLES `vehicle_feature_dict_rel` WRITE;
/*!40000 ALTER TABLE `vehicle_feature_dict_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `vehicle_feature_dict_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_image`
--

DROP TABLE IF EXISTS `vehicle_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_image` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `vehicle_id` bigint unsigned NOT NULL,
  `image_url` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '原图URL',
  `thumb_url` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '缩略图URL',
  `is_main` tinyint(1) DEFAULT '0' COMMENT '是否主图/封面图',
  `sort_no` int DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_vehicle_id` (`vehicle_id`),
  CONSTRAINT `fk_image_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='车辆图片(存图片URL,原图存对象存储/本地磁盘)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_image`
--

LOCK TABLES `vehicle_image` WRITE;
/*!40000 ALTER TABLE `vehicle_image` DISABLE KEYS */;
INSERT INTO `vehicle_image` VALUES (2,4,'/uploads/202600003/pic-e12756f63b8d443b962b54be3ca4bbd0.png','/uploads/202600003/thumb-e12756f63b8d443b962b54be3ca4bbd0.png',1,0,'2026-08-05 09:40:49'),(3,4,'/uploads/202600003/pic-784d37a6336d401e9282c540aec19624.png','/uploads/202600003/thumb-784d37a6336d401e9282c540aec19624.png',0,1,'2026-08-05 09:40:49'),(4,4,'/uploads/202600003/pic-059fe69ad1fa48ccb1f29777368bf1df.png','/uploads/202600003/thumb-059fe69ad1fa48ccb1f29777368bf1df.png',0,2,'2026-08-05 09:40:49'),(5,3,'/uploads/202600002/pic-790c8db16b3743e8aca97ea5ccd8df4e.jpg','/uploads/202600002/thumb-790c8db16b3743e8aca97ea5ccd8df4e.jpg',1,0,'2026-08-05 10:01:20'),(6,3,'/uploads/202600002/pic-555975f677ed40679e5fdc066263158a.jpg','/uploads/202600002/thumb-555975f677ed40679e5fdc066263158a.jpg',0,1,'2026-08-05 10:01:20'),(7,3,'/uploads/202600002/pic-fb2178a96cf54e1b891882ec2f87077d.jpg','/uploads/202600002/thumb-fb2178a96cf54e1b891882ec2f87077d.jpg',0,2,'2026-08-05 10:01:20'),(8,3,'/uploads/202600002/pic-6e187ada892745108c74f979f47791cd.jpg','/uploads/202600002/thumb-6e187ada892745108c74f979f47791cd.jpg',0,3,'2026-08-05 10:01:20'),(9,3,'/uploads/202600002/pic-8738b1f34b3f4fedaea4d3b2083be31a.jpg','/uploads/202600002/thumb-8738b1f34b3f4fedaea4d3b2083be31a.jpg',0,4,'2026-08-05 10:01:20'),(10,3,'/uploads/202600002/pic-10fb0f04dd1240e880d1cba83706e2c7.jpg','/uploads/202600002/thumb-10fb0f04dd1240e880d1cba83706e2c7.jpg',0,5,'2026-08-05 10:01:20'),(11,3,'/uploads/202600002/pic-38052af566da4383b863e0a8254d4676.jpg','/uploads/202600002/thumb-38052af566da4383b863e0a8254d4676.jpg',0,6,'2026-08-05 10:01:20'),(12,3,'/uploads/202600002/pic-dc4fe410393a43bf8806237510be54a0.jpg','/uploads/202600002/thumb-dc4fe410393a43bf8806237510be54a0.jpg',0,7,'2026-08-05 10:01:20'),(13,1,'/uploads/202600001/pic-e4e8b17621f047a2afb659d59ecb0d47.jpg','/uploads/202600001/thumb-e4e8b17621f047a2afb659d59ecb0d47.jpg',1,0,'2026-08-05 10:04:10'),(14,1,'/uploads/202600001/pic-452898ab8c6b496dbcb63db93918ca43.jpg','/uploads/202600001/thumb-452898ab8c6b496dbcb63db93918ca43.jpg',0,1,'2026-08-05 10:04:10'),(15,1,'/uploads/202600001/pic-971aee467fcf4de38222ef46debe6cfc.jpg','/uploads/202600001/thumb-971aee467fcf4de38222ef46debe6cfc.jpg',0,2,'2026-08-05 10:04:10'),(16,1,'/uploads/202600001/pic-a0065e4abe744b84886e2e9c2bbec26f.jpg','/uploads/202600001/thumb-a0065e4abe744b84886e2e9c2bbec26f.jpg',0,3,'2026-08-05 10:04:10'),(17,1,'/uploads/202600001/pic-b98262e32b9c4ffcbbeebe17cc63f5e2.jpg','/uploads/202600001/thumb-b98262e32b9c4ffcbbeebe17cc63f5e2.jpg',0,4,'2026-08-05 10:04:10'),(18,1,'/uploads/202600001/pic-025b14e36b5344aa9e8d2df33b6a3246.jpg','/uploads/202600001/thumb-025b14e36b5344aa9e8d2df33b6a3246.jpg',0,5,'2026-08-05 10:04:10'),(19,5,'/uploads/202600004/pic-774f974a43be4421b1b36514f80fa36c.jpg','/uploads/202600004/thumb-774f974a43be4421b1b36514f80fa36c.jpg',0,2,'2026-08-05 12:06:47'),(20,5,'/uploads/202600004/pic-2b3a6d45742d43b1bd163fd411c5ad48.jpg','/uploads/202600004/thumb-2b3a6d45742d43b1bd163fd411c5ad48.jpg',0,1,'2026-08-05 12:06:47'),(21,5,'/uploads/202600004/pic-14eb0a76ac754ce4a74810af7328d3c5.jpg','/uploads/202600004/thumb-14eb0a76ac754ce4a74810af7328d3c5.jpg',1,0,'2026-08-05 12:06:47'),(22,6,'/uploads/202600005/pic-9936d0ddd995467eaf4cec72c4710b2a.jpg','/uploads/202600005/thumb-9936d0ddd995467eaf4cec72c4710b2a.jpg',0,1,'2026-08-09 08:55:48'),(23,6,'/uploads/202600005/pic-190ea444bdab49cdac2b4fce94dd9c10.png','/uploads/202600005/thumb-190ea444bdab49cdac2b4fce94dd9c10.png',1,0,'2026-08-09 08:55:48'),(29,7,'/uploads/202600006/pic-bc37b0d36a3245fbb71f34a0ef0ab282.jpg','/uploads/202600006/thumb-bc37b0d36a3245fbb71f34a0ef0ab282.jpg',1,0,'2026-08-09 09:34:13'),(30,7,'/uploads/202600006/pic-0bf056f2e7c44b99bcabdbc86b3bcc0c.jpg','/uploads/202600006/thumb-0bf056f2e7c44b99bcabdbc86b3bcc0c.jpg',0,2,'2026-08-09 09:34:13'),(31,7,'/uploads/202600006/pic-1dd8671723d2479080f4ee3dfa652621.jpg','/uploads/202600006/thumb-1dd8671723d2479080f4ee3dfa652621.jpg',0,1,'2026-08-09 09:45:39'),(32,6,'/uploads/202600005/pic-eebd1caa14e1486b90a8c5e025d96357.jpg','/uploads/202600005/thumb-eebd1caa14e1486b90a8c5e025d96357.jpg',0,3,'2026-08-09 09:58:06'),(33,6,'/uploads/202600005/pic-837dbcfeb084468392b34082620403f3.jpg','/uploads/202600005/thumb-837dbcfeb084468392b34082620403f3.jpg',0,2,'2026-08-09 10:06:28'),(34,8,'/uploads/202600007/pic-da48fa9d559043b8a84135b82e5094bc.jpg','/uploads/202600007/thumb-da48fa9d559043b8a84135b82e5094bc.jpg',1,0,'2026-08-09 10:10:42'),(35,8,'/uploads/202600007/pic-47a3cd69c1f640b6a3dd8c2f6bdb8332.jpg','/uploads/202600007/thumb-47a3cd69c1f640b6a3dd8c2f6bdb8332.jpg',0,1,'2026-08-09 10:10:42'),(36,8,'/uploads/202600007/pic-329f95534bc14f7993c3b45d9ddd6ff9.jpg','/uploads/202600007/thumb-329f95534bc14f7993c3b45d9ddd6ff9.jpg',0,2,'2026-08-09 10:10:42'),(37,9,'/uploads/202600008/pic-00050159b4a94755a887e590baba5349.jpg','/uploads/202600008/thumb-00050159b4a94755a887e590baba5349.jpg',1,0,'2026-08-10 02:53:36'),(38,9,'/uploads/202600008/pic-8aebc32e10d64779a2f6c6e6ecaaf089.jpg','/uploads/202600008/thumb-8aebc32e10d64779a2f6c6e6ecaaf089.jpg',0,1,'2026-08-10 02:53:36');
/*!40000 ALTER TABLE `vehicle_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_option`
--

DROP TABLE IF EXISTS `vehicle_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_option` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `vehicle_id` bigint unsigned NOT NULL,
  `option_group` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组: EXTERIOR/INTERIOR/MECHANICAL/SAFETY/ENTERTAINMENT',
  `content` varchar(200) COLLATE utf8mb4_general_ci NOT NULL,
  `sort_no` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_vehicle_group` (`vehicle_id`,`option_group`),
  CONSTRAINT `fk_option_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='车辆出厂选装包';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_option`
--

LOCK TABLES `vehicle_option` WRITE;
/*!40000 ALTER TABLE `vehicle_option` DISABLE KEYS */;
INSERT INTO `vehicle_option` VALUES (17,8,'EXTERIOR','sdf',0),(18,6,'EXTERIOR','white',0),(19,4,'EXTERIOR','exce',0),(20,4,'MECHANICAL','wedsfdsfsdf',1),(22,9,'MECHANICAL','Nissian',0);
/*!40000 ALTER TABLE `vehicle_option` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-23 13:40:25
