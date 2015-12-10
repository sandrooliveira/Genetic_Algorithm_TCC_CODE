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
  `is_atividade_final` bit(1) NOT NULL,
  `nome_atividade` varchar(255) NOT NULL,
  `id_hablidade` int(11) NOT NULL,
  `id_processo` int(11) NOT NULL,
  PRIMARY KEY (`id_atividade`),
  UNIQUE KEY `id_atividade` (`id_atividade`),
  KEY `FKC1B71D07F220ED65` (`id_hablidade`),
  KEY `FKC1B71D073E14782E` (`id_processo`),
  CONSTRAINT `FKC1B71D073E14782E` FOREIGN KEY (`id_processo`) REFERENCES `processo` (`id_processo`),
  CONSTRAINT `FKC1B71D07F220ED65` FOREIGN KEY (`id_hablidade`) REFERENCES `habilidade` (`id_habilidade`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `atividade`
--

LOCK TABLES `atividade` WRITE;
/*!40000 ALTER TABLE `atividade` DISABLE KEYS */;
INSERT INTO `atividade` VALUES (1,'','AT_Finalização',2,1),(2,'\0','AT_Carimbo',1,1);
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
  UNIQUE KEY `id_atividade_ordem` (`id_atividade_ordem`),
  KEY `FK8BE6E6118388BB99` (`id_predecessora`),
  KEY `FK8BE6E6118A6A2F60` (`id_atividade`),
  CONSTRAINT `FK8BE6E6118388BB99` FOREIGN KEY (`id_predecessora`) REFERENCES `atividade` (`id_atividade`),
  CONSTRAINT `FK8BE6E6118A6A2F60` FOREIGN KEY (`id_atividade`) REFERENCES `atividade` (`id_atividade`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `atividade_ordem`
--

LOCK TABLES `atividade_ordem` WRITE;
/*!40000 ALTER TABLE `atividade_ordem` DISABLE KEYS */;
INSERT INTO `atividade_ordem` VALUES (1,1,2);
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
  `disponibilidade` float DEFAULT NULL,
  `nome_costureira` varchar(45) DEFAULT NULL,
  `positionX` int(11) DEFAULT NULL,
  `positionY` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_costureira`),
  UNIQUE KEY `id_costureira` (`id_costureira`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `costureira`
--

LOCK TABLES `costureira` WRITE;
/*!40000 ALTER TABLE `costureira` DISABLE KEYS */;
INSERT INTO `costureira` VALUES (1,0,'Marcelo',2,5),(2,0,'Duda',2,4);
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
  `preco_por_peca` float NOT NULL,
  `tempo_por_peca` int(11) NOT NULL,
  `id_costureira` int(11) NOT NULL,
  `id_habilidade` int(11) NOT NULL,
  PRIMARY KEY (`id_costureira_habilidade`),
  UNIQUE KEY `id_costureira_habilidade` (`id_costureira_habilidade`),
  KEY `FK2E457D3D19D19C64` (`id_habilidade`),
  KEY `FK2E457D3D3DD66CE8` (`id_costureira`),
  CONSTRAINT `FK2E457D3D19D19C64` FOREIGN KEY (`id_habilidade`) REFERENCES `habilidade` (`id_habilidade`),
  CONSTRAINT `FK2E457D3D3DD66CE8` FOREIGN KEY (`id_costureira`) REFERENCES `costureira` (`id_costureira`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `costureira_habilidade`
--

LOCK TABLES `costureira_habilidade` WRITE;
/*!40000 ALTER TABLE `costureira_habilidade` DISABLE KEYS */;
INSERT INTO `costureira_habilidade` VALUES (1,0,0,1,1),(2,12,2,2,2);
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
  PRIMARY KEY (`id_habilidade`),
  UNIQUE KEY `id_habilidade` (`id_habilidade`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habilidade`
--

LOCK TABLES `habilidade` WRITE;
/*!40000 ALTER TABLE `habilidade` DISABLE KEYS */;
INSERT INTO `habilidade` VALUES (1,'Carimbo'),(2,'Finalização');
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
  `cliente` varchar(45) DEFAULT NULL,
  `data_entrega` datetime DEFAULT NULL,
  `nome_processo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_processo`),
  UNIQUE KEY `id_processo` (`id_processo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `processo`
--

LOCK TABLES `processo` WRITE;
/*!40000 ALTER TABLE `processo` DISABLE KEYS */;
INSERT INTO `processo` VALUES (1,'te','2015-12-09 23:02:33','te');
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

-- Dump completed on 2015-12-09 23:05:24
