-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: incafit_db
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `socios`
--

DROP TABLE IF EXISTS `socios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `socios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dni` varchar(10) NOT NULL,
  `email` varchar(255) NOT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `fecha_registro` date DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` enum('ADMIN','USUARIO') NOT NULL DEFAULT 'USUARIO',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `membresia_id` bigint DEFAULT NULL,
  `fecha_inicio_membresia` date DEFAULT NULL,
  `fecha_fin_membresia` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKk00ad11od89ejh58gm7tib51r` (`dni`),
  UNIQUE KEY `UK6lsbbcv0o607ad0cbb1v5wxlt` (`email`),
  UNIQUE KEY `dni` (`dni`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `dni_2` (`dni`),
  UNIQUE KEY `email_2` (`email`),
  KEY `FK4w3sdpk251dxde78213n3vf0h` (`membresia_id`),
  CONSTRAINT `FK4w3sdpk251dxde78213n3vf0h` FOREIGN KEY (`membresia_id`) REFERENCES `membresias` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `socios`
--

LOCK TABLES `socios` WRITE;
/*!40000 ALTER TABLE `socios` DISABLE KEYS */;
INSERT INTO `socios` VALUES (2,'12345678A','test@example.com','600123456','2025-09-10','Usuario Test','$2a$10$r3k4I5q6w7e8r9t0y1u2vOcQdReSfTgUhViWjXkYlZmAnBoCpDqEs','USUARIO',1,1,'2025-10-19','2025-11-18'),(3,'74085564','nikkmed805@gmail.com','600987653','2025-09-29','Nikolas Adriano Medina Ricra','$2a$10$4DxZXb5Ys4y9chw2tIClFuuP7tZlafoAwyiQUTicTy0fsYHjGqX2u','ADMIN',1,2,'2025-10-19','2026-01-17'),(4,'12345678','prueba@yopmail.com',NULL,'2025-10-14','Prueba Prueba Prueba Pruebaa','123456','USUARIO',1,2,NULL,NULL),(6,'123456777','holainca@yopmail.com','666999330','2025-10-21','Hola','$2a$10$JucMYn4Jc1anijZraDtKh..yqx0Q5j4YXlRwu8F2GzrM/pL0Zyrji','USUARIO',1,1,'2025-12-10','2026-01-08'),(8,'740855643','testeo@yopmail.com','357357758','2025-10-30','TESTEO testeo','$2a$10$wsajuKnoiPOrnZxXZOGZcO.WIFCWDpzkKun3fK16jt9DPdqWu8nfS','USUARIO',1,3,NULL,NULL),(9,'74085562','holatest@yopmail.com','603100222','2025-10-30','HOLA','$2a$10$i8hc3k2AYYbb.uJPKP1YkuQZxDbxrbGtqqotkEDH0FjgVP1PmAJYu','USUARIO',1,3,NULL,NULL),(11,'3914119241','final@yopmail.com',NULL,'2025-11-04','final final','12345678','USUARIO',1,1,NULL,NULL),(12,'00000007','admindemo@incafit.com','600777888','2025-11-04','Admin Demo','$2a$10$M7AlYmPn1QIPDultONJ2XOq8c6PfIL1eM/jDOxKMK7gf/CIEvdOGO','ADMIN',1,NULL,NULL,NULL),(13,'123456781','Hola@yopmail.com','603030242','2025-11-07','Holaa','$2a$10$Sgd5LDMladLyh172ElTGoeDjyFLx7kngIswQrWRfx3NvKWk4Mt.yG','USUARIO',1,1,NULL,NULL),(14,'74085533','finaltest@yopmail.com','123456711','2025-11-10','Final','$2a$10$Pt7skOOm2pfPv9HO7dojKeojs8qzEHQ5PqUULOnJ77j0smDNJLBJW','USUARIO',1,1,NULL,NULL),(15,'12341412A','nikkmed@hotmail.com','666303444','2025-11-13','Niko','$2a$10$m.O5PHvOLJXwuI9xSu3qPe6f1wQdr61Bbp5shnEckm8NXyDa8TAxu','USUARIO',1,1,NULL,NULL);
/*!40000 ALTER TABLE `socios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-13 18:42:43
