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
-- Table structure for table `clases`
--

DROP TABLE IF EXISTS `clases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clases` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `capacidad_maxima` int NOT NULL DEFAULT '1',
  `instructor_id` bigint DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `duracion_minutos` int DEFAULT NULL,
  `dias_semana` varchar(255) DEFAULT NULL,
  `activo` bit(1) NOT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `precio_adicional` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `instructor_id` (`instructor_id`),
  CONSTRAINT `clases_ibfk_1` FOREIGN KEY (`instructor_id`) REFERENCES `instructores` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clases`
--

LOCK TABLES `clases` WRITE;
/*!40000 ALTER TABLE `clases` DISABLE KEYS */;
INSERT INTO `clases` VALUES (1,'Yoga','Clase de relajacion y flexibilidad',20,6,'15:45:00',60,'MONDAY,WEDNESDAY,FRIDAY',_binary '',NULL,NULL,NULL),(2,'Spinning','Clase de ciclismo intenso',15,2,'18:00:00',45,'TUESDAY,THURSDAY',_binary '',NULL,NULL,NULL),(3,'Pilates','Fortalecimiento del core y flexibilidad',12,6,'10:00:00',50,'MONDAY,WEDNESDAY',_binary '',NULL,NULL,NULL),(4,'HIIT','Entrenamiento de alta intensidad',10,2,'19:30:00',30,'TUESDAY,THURSDAY',_binary '',NULL,NULL,NULL),(5,'Musculacion','Entrenamiento con pesas',8,3,'07:00:00',90,'MONDAY,WEDNESDAY,FRIDAY',_binary '',NULL,NULL,NULL),(7,'Defensa Personal','Curso de defensa personal de 3 meses',15,2,'19:00:00',90,'MONDAY,WEDNESDAY,FRIDAY',_binary '','2025-10-19','2026-01-17',25.00),(8,'Yoga','Clase de relajacion y flexibilidad',20,6,'18:30:00',60,'TUESDAY,THURSDAY',_binary '',NULL,NULL,NULL),(9,'Spinning','Clase de ciclismo intenso',15,2,'18:00:00',45,'MONDAY,WEDNESDAY,FRIDAY',_binary '',NULL,NULL,NULL),(10,'Pilates','Fortalecimiento del core y flexibilidad',12,6,'10:00:00',50,'TUESDAY,THURSDAY',_binary '',NULL,NULL,NULL),(11,'HIIT','Entrenamiento de alta intensidad',10,2,'19:30:00',30,'MONDAY,WEDNESDAY,FRIDAY',_binary '',NULL,NULL,NULL),(12,'Musculacion','Entrenamiento con pesas',8,3,'07:00:00',90,'TUESDAY,THURSDAY',_binary '',NULL,NULL,NULL),(13,'Zumba','Baile y cardio',25,4,'20:00:00',60,'MONDAY,WEDNESDAY,FRIDAY',_binary '',NULL,NULL,NULL),(14,'Defensa Personal','Curso de defensa personal de 3 meses',15,3,'19:00:00',90,'MONDAY,WEDNESDAY,FRIDAY',_binary '','2025-10-19','2026-01-17',25.00);

/*!40000 ALTER TABLE `clases` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-03 14:16:24
