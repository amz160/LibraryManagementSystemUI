-- MySQL dump 10.13  Distrib 8.0.40, for macos14 (x86_64)
--
-- Host: localhost    Database: LibraryDB
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Books`
--

DROP TABLE IF EXISTS `Books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Books` (
  `BookID` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(255) DEFAULT NULL,
  `Author` varchar(100) DEFAULT NULL,
  `Genre` varchar(100) DEFAULT NULL,
  `PublicationYear` int DEFAULT NULL,
  `Availability` tinyint(1) DEFAULT '1',
  `dueDate` date DEFAULT NULL,
  PRIMARY KEY (`BookID`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Books`
--

LOCK TABLES `Books` WRITE;
/*!40000 ALTER TABLE `Books` DISABLE KEYS */;
INSERT INTO `Books` VALUES (2,'The Hunger Games','Suzanne Collins','YA',2008,1,NULL),(3,'Twilight','Stephanie Meyer','YA',2005,1,NULL),(5,'To Kill a Mockingbird','Harper Lee','Fiction',1960,1,NULL),(6,'Pride and Prejudice','Jane Austen','Classic',1813,1,NULL),(7,'Harry Potter and the Sorcerer\'s Stone','J.K. Rowling','Fantasy',1997,1,NULL),(9,'Frankenstein','Mary Shelley','Gothic',1818,1,NULL),(10,'Harry Potter and the Chamber of Secrets','J.K. Rowling','Fantasy',1998,1,NULL),(11,'Harry Potter and the Prisoner of Azkaban','J.K. Rowling','Fantasy',1999,1,NULL),(13,'1984','George Orwell','Dystopian',1949,1,NULL),(14,'Pride and Prejudice','Jane Austen','Romance',1813,1,NULL),(15,'The Great Gatsby','F. Scott Fitzgerald','Classic',1925,1,NULL),(16,'The Catcher in the Rye','J.D. Salinger','Classic',1951,1,NULL),(17,'The Hobbit','J.R.R. Tolkien','Fantasy',1937,1,NULL),(18,'The Alchemist','Paulo Coelho','Adventure',1988,1,NULL),(19,'The Lord of the Rings','J.R.R. Tolkien','Fantasy',1954,1,NULL),(20,'Brave New World','Aldous Huxley','Dystopian',1932,1,NULL),(21,'The Road','Cormac McCarthy','Post-Apocalyptic',2006,1,NULL),(22,'The Kite Runner','Khaled Hosseini','Drama',2003,1,NULL),(23,'The Da Vinci Code','Dan Brown','Thriller',2003,1,NULL),(24,'The Girl with the Dragon Tattoo','Stieg Larsson','Mystery',2005,1,NULL),(25,'Little Women','Louisa May Alcott','Classic',1868,1,NULL),(26,'The Fault in Our Stars','John Green','YA',2012,1,NULL),(27,'Jane Eyre','Charlotte Bronte','Classic',1847,1,NULL),(28,'Wuthering Heights','Emily Bronte','Classic',1847,1,NULL),(29,'Animal Farm','George Orwell','Satire',1945,1,NULL),(30,'Gone Girl','Gillian Flynn','Thriller',2012,1,NULL),(31,'Moby Dick','Herman Melville','Classic',1851,1,NULL),(33,'A Game of Thrones','George R.R. Martin','Fantasy',1996,1,NULL),(34,'The Book Thief','Markus Zusak','Historical Fiction',2005,1,NULL),(35,'War and Peace','Leo Tolstoy','Historical Fiction',1869,1,NULL),(36,'Crime and Punishment','Fyodor Dostoevsky','Classic',1866,1,NULL),(37,'Great Expectations','Charles Dickens','Classic',1861,1,NULL),(38,'The Shining','Stephen King','Horror',1977,1,NULL),(39,'The Picture of Dorian Gray','Oscar Wilde','Classic',1890,1,NULL),(40,'The Count of Monte Cristo','Alexandre Dumas','Adventure',1844,1,NULL),(41,'Dracula','Bram Stoker','Horror',1897,1,NULL),(42,'The Handmaid\'s Tale','Margaret Atwood','Dystopian',1985,1,NULL),(43,'The Grapes of Wrath','John Steinbeck','Historical Fiction',1939,1,NULL),(44,'One Hundred Years of Solitude','Gabriel Garcia Marquez','Magical Realism',1967,1,NULL),(45,'The Catch-22','Joseph Heller','Satire',1961,1,NULL),(46,'Frankenstein','Mary Shelley','Science Fiction',1818,1,NULL),(47,'The Chronicles of Narnia','C.S. Lewis','Fantasy',1950,1,NULL),(48,'The Bell Jar','Sylvia Plath','Autobiographical Fiction',1963,1,NULL),(49,'The Iliad','Homer','Epic',-750,1,NULL),(50,'The Color Purple','Alice Walker','Historical Fiction',1982,1,NULL),(51,'The Stranger','Albert Camus','Philosophical',1942,1,NULL),(52,'The Maze Runner','James Dashner','YA',2009,1,NULL),(53,'The Stand','Stephen King','Horror',1978,1,NULL),(54,'Fahrenheit 451','Ray Bradbury','Dystopian',1953,1,NULL),(55,'Memoirs of a Geisha','Arthur Golden','Historical Fiction',1997,1,NULL),(56,'The Perks of Being a Wallflower','Stephen Chbosky','YA',1999,1,NULL),(57,'The Time Traveler\'s Wife','Audrey Niffenegger','Science Fiction',2003,1,NULL);
/*!40000 ALTER TABLE `Books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Patrons`
--

DROP TABLE IF EXISTS `Patrons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Patrons` (
  `PatronID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `AccountCreated` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isLibrarian` tinyint(1) DEFAULT '0',
  `Password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PatronID`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Patrons`
--

LOCK TABLES `Patrons` WRITE;
/*!40000 ALTER TABLE `Patrons` DISABLE KEYS */;
INSERT INTO `Patrons` VALUES (3,'Jane Doe','janedoe@email.com','2024-10-28 04:43:19',0,'janePassword'),(4,'Jack Smith','jacksmith@email.com','2024-10-28 04:43:19',0,'jackPassword'),(5,'Betty Jones','bettyjones@email.com','2024-10-28 05:35:20',1,'bettyPassword'),(6,'Noah Brown','noahbrown@email.com','2024-10-28 05:35:20',1,'noahPassword'),(7,'Lynn Miller','lynnmiller@email.com','2024-10-28 05:35:20',1,'lynnPassword'),(11,'Lola Garcia','lolagarcia@email.com','2024-10-28 06:11:15',0,'lolaPassword'),(12,'Sam Davis','samdavis@email.com','2024-10-28 07:59:26',0,'samPassword'),(13,'Sara Lee','saralee@email.com','2024-10-30 04:19:11',0,'saraPassword');
/*!40000 ALTER TABLE `Patrons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Transactions`
--

DROP TABLE IF EXISTS `Transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Transactions` (
  `TransactionID` int NOT NULL AUTO_INCREMENT,
  `PatronID` int NOT NULL,
  `BookID` int NOT NULL,
  `BorrowDate` date NOT NULL,
  `DueDate` date DEFAULT NULL,
  `Returned` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`TransactionID`),
  UNIQUE KEY `unique_book_patron` (`PatronID`,`BookID`,`Returned`),
  KEY `BookID` (`BookID`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`PatronID`) REFERENCES `Patrons` (`PatronID`),
  CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`BookID`) REFERENCES `Books` (`BookID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Transactions`
--

LOCK TABLES `Transactions` WRITE;
/*!40000 ALTER TABLE `Transactions` DISABLE KEYS */;
INSERT INTO `Transactions` VALUES (27,4,5,'2024-11-02','2024-11-16',1),(28,4,46,'2024-11-02','2024-11-16',1),(29,4,3,'2024-11-02','2024-11-16',1);
/*!40000 ALTER TABLE `Transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Wishlist`
--

DROP TABLE IF EXISTS `Wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Wishlist` (
  `WishlistID` int NOT NULL AUTO_INCREMENT,
  `PatronID` int NOT NULL,
  `BookID` int NOT NULL,
  PRIMARY KEY (`WishlistID`),
  KEY `wishlist_fk_patron` (`PatronID`),
  KEY `fk_wishlist_book` (`BookID`),
  CONSTRAINT `fk_wishlist_book` FOREIGN KEY (`BookID`) REFERENCES `Books` (`BookID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Wishlist`
--

LOCK TABLES `Wishlist` WRITE;
/*!40000 ALTER TABLE `Wishlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `Wishlist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-02 20:04:11
