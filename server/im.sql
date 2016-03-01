-- MySQL dump 10.13  Distrib 5.5.41, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: IM
-- ------------------------------------------------------
-- Server version	5.5.41-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Departments`
--

DROP TABLE IF EXISTS `Departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Departments` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Departments`
--

LOCK TABLES `Departments` WRITE;
/*!40000 ALTER TABLE `Departments` DISABLE KEYS */;
/*!40000 ALTER TABLE `Departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MediaMessages`
--

DROP TABLE IF EXISTS `MediaMessages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MediaMessages` (
  `path` varchar(255) NOT NULL,
  `subject` varchar(255) NOT NULL,
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_jun09rk647jlp1s3bbbx477j4` FOREIGN KEY (`id`) REFERENCES `Messages` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MediaMessages`
--

LOCK TABLES `MediaMessages` WRITE;
/*!40000 ALTER TABLE `MediaMessages` DISABLE KEYS */;
/*!40000 ALTER TABLE `MediaMessages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Messages`
--

DROP TABLE IF EXISTS `Messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Messages` (
  `id` bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  `sent` bit(1) NOT NULL,
  `i_user_from` bigint(20) NOT NULL,
  `i_user_to` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_qeryka4ian7a9k3p0h9ehbmup` (`i_user_from`),
  KEY `FK_7tdq6qspgc9g0tc93l24njw6s` (`i_user_to`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Messages`
--

LOCK TABLES `Messages` WRITE;
/*!40000 ALTER TABLE `Messages` DISABLE KEYS */;
INSERT INTO `Messages` VALUES (1,'2015-03-23 01:42:46','',1,2),(2,'2015-03-23 01:52:59','',1,2),(3,'2015-03-23 01:53:36','',1,2),(4,'2015-03-23 01:56:40','',1,2),(5,'2014-07-23 01:59:56','',2,1),(6,'2015-03-23 23:53:48','',2,1),(7,'2015-03-24 00:02:06','',1,2),(8,'2015-03-24 00:06:07','',1,2),(9,'2015-03-24 00:11:03','',1,2),(10,'2015-03-24 00:18:15','',1,2),(11,'2015-03-24 00:21:30','',1,2),(12,'2015-03-24 00:32:16','',1,2),(13,'2015-03-24 00:32:34','',1,2),(14,'2015-03-24 00:32:59','',2,1),(15,'2015-03-24 13:32:06','',1,2),(16,'2015-03-24 13:32:15','',1,2),(17,'2015-03-24 13:32:18','',1,2),(18,'2015-03-24 13:32:22','',2,1),(19,'2015-03-24 13:32:27','',1,2),(20,'2015-03-24 13:37:10','',1,2),(21,'2015-03-24 13:37:15','',1,2),(22,'2015-03-26 21:04:20','',1,2),(23,'2015-03-26 21:05:22','',1,2),(24,'2015-03-26 21:05:25','',1,2),(25,'2015-03-26 21:05:53','',1,2),(26,'2015-03-26 21:26:32','',1,2),(27,'2015-03-26 21:26:35','',1,2),(28,'2015-03-26 21:27:37','',1,2),(29,'2015-03-26 21:27:40','',1,2),(30,'2015-03-26 21:27:42','',1,2),(31,'2015-03-26 21:27:45','',1,2),(32,'2015-03-26 21:27:53','',2,1),(33,'2015-03-26 21:45:18','',1,2),(34,'2015-03-26 21:45:28','',1,2),(35,'2015-03-26 21:45:53','',1,2),(36,'2015-03-26 21:46:14','',1,2),(37,'2015-03-26 21:46:22','',1,2),(38,'2015-03-26 21:47:14','',1,2),(39,'2015-03-26 21:48:26','',1,2),(40,'2015-03-26 21:57:41','',1,2),(41,'2015-03-26 21:57:49','',1,2),(42,'2015-03-26 21:57:59','',1,2),(43,'2015-03-26 21:58:04','',1,2),(44,'2015-03-26 21:58:07','',1,2),(45,'2015-03-26 22:56:51','',2,1),(46,'2015-03-26 22:56:54','',2,1),(47,'2015-03-27 08:10:34','',1,2),(48,'2015-03-27 08:10:43','',1,2),(49,'2015-03-27 08:11:05','',1,2),(50,'2015-03-27 08:11:09','',2,1),(51,'2015-03-27 12:57:23','',1,2),(52,'2015-03-27 13:02:38','\0',1,2),(53,'2015-03-27 13:15:37','\0',1,2),(54,'2015-03-27 22:38:06','',1,2),(55,'2015-03-28 01:15:11','\0',1,2),(56,'2015-03-29 16:53:26','\0',2,1),(57,'2015-03-29 18:34:09','\0',2,1),(58,'2015-03-29 18:34:09','\0',2,1),(61,'2015-03-29 23:03:40','\0',2,1),(62,'2015-03-29 23:05:21','\0',2,1),(63,'2015-03-29 23:45:02','\0',2,1),(64,'2015-03-29 23:48:02','\0',1,2),(65,'2015-03-29 23:48:53','\0',1,2),(66,'2015-03-30 21:03:30','\0',6,4),(67,'2015-03-30 21:05:29','',1,2),(68,'2015-03-30 21:05:42','',2,1),(69,'2015-03-30 21:05:52','',1,2),(70,'2015-03-30 21:15:58','',1,2),(71,'2015-03-30 21:17:24','',2,1),(72,'2015-03-30 21:21:44','',2,1),(73,'2015-03-30 21:22:51','',2,1),(74,'2015-03-30 21:24:00','',2,1),(75,'2015-03-30 21:24:07','',2,1),(76,'2015-03-30 21:24:42','',2,1),(77,'2015-03-30 22:50:35','',1,2),(78,'2015-03-30 22:50:44','',1,2),(79,'2015-03-30 23:19:35','',1,2),(80,'2015-03-30 23:19:48','',2,1),(81,'2015-03-30 23:21:52','\0',1,2),(82,'2015-03-31 23:09:18','',1,2),(83,'2015-03-31 23:09:33','',1,2),(84,'2015-03-31 23:09:49','',2,1),(85,'2015-04-01 00:48:17','\0',1,2),(86,'2015-04-01 00:49:28','\0',1,2),(87,'2015-04-01 00:51:07','\0',1,2),(88,'2015-04-01 09:45:03','\0',1,2),(89,'2015-04-01 09:53:21','',1,2),(90,'2015-04-01 09:54:30','',2,1),(91,'2015-04-01 09:54:38','',1,2),(92,'2015-04-01 10:05:33','',1,2),(93,'2015-04-01 10:05:55','',1,2),(94,'2015-04-01 12:08:41','',1,2),(95,'2015-04-01 12:09:05','\0',1,2),(96,'2015-04-01 12:10:06','\0',1,2);
/*!40000 ALTER TABLE `Messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TextMessages`
--

DROP TABLE IF EXISTS `TextMessages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TextMessages` (
  `content` varchar(255) NOT NULL,
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_nk2e77nl77i0sooiapn28b8vf` FOREIGN KEY (`id`) REFERENCES `Messages` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TextMessages`
--

LOCK TABLES `TextMessages` WRITE;
/*!40000 ALTER TABLE `TextMessages` DISABLE KEYS */;
/*!40000 ALTER TABLE `TextMessages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Users` (
  `id` bigint(20) NOT NULL,
  `ip` varchar(255) NOT NULL,
  `login` varchar(255) NOT NULL,
  `mtime` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `port` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `surname` varchar(255) NOT NULL,
  `i_department` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_79ht9tqcll25o881aand51k20` (`i_department`),
  CONSTRAINT `FK_79ht9tqcll25o881aand51k20` FOREIGN KEY (`i_department`) REFERENCES `Departments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VoiceMessages`
--

DROP TABLE IF EXISTS `VoiceMessages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VoiceMessages` (
  `path` varchar(255) NOT NULL,
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_nagjwtdo9vjqf8j48bbk7pjdg` FOREIGN KEY (`id`) REFERENCES `Messages` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VoiceMessages`
--

LOCK TABLES `VoiceMessages` WRITE;
/*!40000 ALTER TABLE `VoiceMessages` DISABLE KEYS */;
/*!40000 ALTER TABLE `VoiceMessages` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-04-17 19:52:04
