-- MySQL dump 10.13  Distrib 5.6.24, for Win32 (x86)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	5.6.26-log

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
-- Table structure for table `atividade`
--

DROP TABLE IF EXISTS `atividade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atividade` (
  `id_atividade` int(11) NOT NULL AUTO_INCREMENT,
  `id_processo` int(11) NOT NULL,
  `id_hablidade` int(11) NOT NULL,
  `nome_atividade` varchar(255) NOT NULL,
  `is_atividade_final` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_atividade`),
  KEY `fk_processos_habilidades_processos1_idx` (`id_processo`),
  KEY `fk_processos_habilidades_partes_calca1_idx` (`id_hablidade`),
  CONSTRAINT `fk_processos_habilidades_partes_calca1` FOREIGN KEY (`id_hablidade`) REFERENCES `habilidade` (`id_habilidade`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_processos_habilidades_processos1` FOREIGN KEY (`id_processo`) REFERENCES `processo` (`id_processo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=297 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `atividade`
--

LOCK TABLES `atividade` WRITE;
/*!40000 ALTER TABLE `atividade` DISABLE KEYS */;
INSERT INTO `atividade` VALUES (56,80,1,'AT_Finalização',1),(170,80,5,'AT_Carimbo',0),(288,80,6,'AT_Frente',0),(294,80,4,'AT_Bolso',0),(295,80,7,'AT_Ziper',0),(296,80,8,'AT_Traz',0);
/*!40000 ALTER TABLE `atividade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `atividade_ordem`
--

DROP TABLE IF EXISTS `atividade_ordem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atividade_ordem` (
  `id_atividade_ordem` int(11) NOT NULL AUTO_INCREMENT,
  `id_atividade` int(11) NOT NULL,
  `id_predecessora` int(11) NOT NULL,
  PRIMARY KEY (`id_atividade_ordem`),
  KEY `fk_atividades_ordens_atividade1_idx` (`id_atividade`),
  KEY `fk_atividades_ordens_atividade2_idx` (`id_predecessora`),
  CONSTRAINT `fk_atividades_ordens_atividade1` FOREIGN KEY (`id_atividade`) REFERENCES `atividade` (`id_atividade`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_atividades_ordens_atividade2` FOREIGN KEY (`id_predecessora`) REFERENCES `atividade` (`id_atividade`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=684 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `atividade_ordem`
--

LOCK TABLES `atividade_ordem` WRITE;
/*!40000 ALTER TABLE `atividade_ordem` DISABLE KEYS */;
INSERT INTO `atividade_ordem` VALUES (682,56,294),(683,294,170);
/*!40000 ALTER TABLE `atividade_ordem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `costureira`
--

DROP TABLE IF EXISTS `costureira`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `costureira` (
  `id_costureira` int(11) NOT NULL AUTO_INCREMENT,
  `nome_costureira` varchar(45) DEFAULT NULL,
  `disponibilidade` float DEFAULT NULL,
  `positionX` int(11) DEFAULT NULL,
  `positionY` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_costureira`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `costureira`
--

LOCK TABLES `costureira` WRITE;
/*!40000 ALTER TABLE `costureira` DISABLE KEYS */;
INSERT INTO `costureira` VALUES (1,'Joana',1,0,0),(2,'Cida',0.5,0,0),(3,'Marta',1,0,0),(4,'Roberta',1,5,6),(5,'Maria',1,0,0),(6,'Tereza',1,2,4),(7,'Geralda',1,0,0),(8,'Silvia',1,0,0),(9,'Bete',1,0,0),(10,'Dita',1,0,0),(11,'Fia',1,0,0),(12,'Lu',1,0,0),(13,'Valdene',1,0,0),(14,'Josi',1,3,4),(15,'Ana',1,0,0),(16,'Luana',1,0,0),(17,'Marcelo',1,8,2);
/*!40000 ALTER TABLE `costureira` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `costureira_habilidade`
--

DROP TABLE IF EXISTS `costureira_habilidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `costureira_habilidade` (
  `id_costureira_habilidade` int(11) NOT NULL AUTO_INCREMENT,
  `id_habilidade` int(11) NOT NULL,
  `id_costureira` int(11) NOT NULL,
  `tempo_por_peca` int(11) NOT NULL,
  PRIMARY KEY (`id_costureira_habilidade`),
  KEY `fk_costureiras_habilidades_partes_calca_idx` (`id_habilidade`),
  KEY `fk_costureiras_habilidades_costureiras1_idx` (`id_costureira`),
  CONSTRAINT `fk_costureiras_habilidades_costureiras1` FOREIGN KEY (`id_costureira`) REFERENCES `costureira` (`id_costureira`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_costureiras_habilidades_partes_calca` FOREIGN KEY (`id_habilidade`) REFERENCES `habilidade` (`id_habilidade`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `costureira_habilidade`
--

LOCK TABLES `costureira_habilidade` WRITE;
/*!40000 ALTER TABLE `costureira_habilidade` DISABLE KEYS */;
INSERT INTO `costureira_habilidade` VALUES (13,6,4,2),(16,6,6,2),(17,8,3,1),(18,8,1,2),(19,8,7,3),(20,8,8,4),(21,8,6,5),(22,8,5,6),(23,7,1,1),(24,7,12,2),(25,7,11,3),(26,7,13,4),(31,4,13,1),(32,4,3,2),(33,4,10,3),(34,1,14,1),(35,5,17,0);
/*!40000 ALTER TABLE `costureira_habilidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `habilidade`
--

DROP TABLE IF EXISTS `habilidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `habilidade` (
  `id_habilidade` int(11) NOT NULL AUTO_INCREMENT,
  `nome_habilidade` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_habilidade`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habilidade`
--

LOCK TABLES `habilidade` WRITE;
/*!40000 ALTER TABLE `habilidade` DISABLE KEYS */;
INSERT INTO `habilidade` VALUES (1,'Finalização'),(4,'Bolso'),(5,'Carimbo'),(6,'Frente'),(7,'Ziper'),(8,'Traz'),(9,'Bordado'),(10,'Chique');
/*!40000 ALTER TABLE `habilidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `processo`
--

DROP TABLE IF EXISTS `processo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `processo` (
  `id_processo` int(11) NOT NULL AUTO_INCREMENT,
  `nome_processo` varchar(45) NOT NULL,
  `cliente` varchar(45) NOT NULL,
  `data_entrega` date NOT NULL,
  PRIMARY KEY (`id_processo`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `processo`
--

LOCK TABLES `processo` WRITE;
/*!40000 ALTER TABLE `processo` DISABLE KEYS */;
INSERT INTO `processo` VALUES (80,'Calça X','Sandro','2015-09-20');
/*!40000 ALTER TABLE `processo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-10-01 22:21:44
