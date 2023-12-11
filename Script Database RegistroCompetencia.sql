CREATE DATABASE  IF NOT EXISTS `registrocompetencia` /*!40100 DEFAULT CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `registrocompetencia`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: registrocompetencia
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `participaciones`
--

DROP TABLE IF EXISTS `participaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `participaciones` (
  `nickname` varchar(255) COLLATE utf8mb3_spanish_ci NOT NULL,
  `codigo_participacion` int DEFAULT NULL,
  `puntuacion_final` double DEFAULT NULL,
  `tiempo` int DEFAULT NULL,
  PRIMARY KEY (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participaciones`
--

LOCK TABLES `participaciones` WRITE;
/*!40000 ALTER TABLE `participaciones` DISABLE KEYS */;
INSERT INTO `participaciones` VALUES ('alejandro_rz',1234,-1,0),('ana_gar',1234,-1,0),('anai_jmz',1234,-1,0),('and_per',1234,78,85),('car_her',1234,90,110),('car_lop',1234,94,90),('carlos_sch',1234,-1,0),('die_fer',1234,82,105),('diego_her',1234,-1,0),('fernandodc',1234,-1,0),('javier_rd',1234,-1,0),('jua_mar',1234,82,75),('lau_gon',1234,90,80),('laura_pz',1234,-1,0),('luis_mtz',1234,-1,0),('mar_rod',1234,88,100),('maria_lp',1234,-1,0),('pau_gom',1234,78,95),('sof_dia',1234,88,125),('sofia_gz',1234,-1,0),('val_mar',1234,94,115);
/*!40000 ALTER TABLE `participaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `cedula` int NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `apellido` varchar(50) COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `dia` int DEFAULT NULL,
  `mes` varchar(45) COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `anio` int DEFAULT NULL,
  `lugar_residencia` varchar(255) COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`cedula`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (12525,'a','a','ferd98000@gmail.com',1,'Enero',2023,'aa'),(111111111,'Laura','González','laura.gonzalez@example.com',15,'Marzo',1995,'Limon'),(119090957,'Fernando','Delgado','ferd9800@gmail.com',21,'Julio',2004,'Heredia'),(154897632,'Javier','Rodríguez','javier.rodriguez@example.com',12,'Mayo',1995,'Alajuela'),(205438791,'Carlos','Hernández','carlos.hernandez@example.com',10,'Febrero',1998,'San José'),(210943876,'Sofía','González','sofia.gonzalez@example.com',7,'Octubre',1997,'San José'),(222222222,'Andrés','Pérez','andres.perez@example.com',8,'Junio',1989,'Guanacaste'),(303451289,'Ana','García','ana.garcia@example.com',21,'Julio',2004,'Heredia'),(333333333,'María','Rodríguez','maria.rodriguez@example.com',20,'Abril',2001,'San José'),(409287516,'Sofía','Díaz','sofia.diaz@example.com',28,'Junio',1996,'Heredia'),(435789012,'Diego','Herrera','diego.herrera@example.com',3,'Marzo',2000,'Cartago'),(444444444,'Juan','Martínez','juan.martinez@example.com',11,'Enero',1997,'Heredia'),(452378901,'Carlos','Sánchez','carlos.sanchez@example.com',8,'Abril',1988,'Puntarenas'),(502134786,'Valentina','Martín','valentina.martin@example.com',5,'Noviembre',2002,'Puntarenas'),(509768432,'Luis','Martínez','luis.martinez@example.com',15,'Agosto',1999,'San José'),(555555555,'Carolina','López','carolina.lopez@example.com',30,'Septiembre',1992,'Puntarenas'),(601827349,'Paula','Gómez','paula.gomez@example.com',3,'Abril',2000,'Cartago'),(623401987,'Anaí','Jiménez','anai.jimenez@example.com',19,'Febrero',1992,'Guanacaste'),(708934561,'Laura','Pérez','laura.perez@example.com',5,'Septiembre',2001,'Cartago'),(790124356,'Alejandro','Ramírez','alejandro.ramirez@example.com',25,'Diciembre',1986,'Limón'),(803246915,'Diego','Fernández','diego.fernandez@example.com',17,'Septiembre',1993,'Alajuela'),(872345612,'María','López','maria.lopez@example.com',30,'Noviembre',1990,'Heredia');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'registrocompetencia'
--

--
-- Dumping routines for database 'registrocompetencia'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-11 10:08:17
