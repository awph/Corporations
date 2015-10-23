CREATE DATABASE  IF NOT EXISTS `corporations` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `corporations`;
-- MySQL dump 10.13  Distrib 5.6.11, for Win32 (x86)
--
-- Host: localhost    Database: corporations
-- ------------------------------------------------------
-- Server version	5.1.71

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
-- Table structure for table `alliance`
--

DROP TABLE IF EXISTS `alliance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alliance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `solicitor` bigint(20) unsigned NOT NULL,
  `solicitee` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_alliance` (`solicitee`,`solicitor`),
  KEY `idx_alliance_solicitor` (`solicitor`),
  KEY `idx_alliance_solicitee` (`solicitee`),
  CONSTRAINT `fk_alliance` FOREIGN KEY (`solicitor`) REFERENCES `player` (`user_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_alliance_solicitee` FOREIGN KEY (`solicitee`) REFERENCES `player` (`user_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `alliances`
--

DROP TABLE IF EXISTS `alliances`;
/*!50001 DROP VIEW IF EXISTS `alliances`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `alliances` (
  `user_id` tinyint NOT NULL,
  `nb_alliance` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `allies`
--

DROP TABLE IF EXISTS `allies`;
/*!50001 DROP VIEW IF EXISTS `allies`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `allies` (
  `user_id` tinyint NOT NULL,
  `nb_alliance` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `detailed_territory`
--

DROP TABLE IF EXISTS `detailed_territory`;
/*!50001 DROP VIEW IF EXISTS `detailed_territory`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `detailed_territory` (
  `id` tinyint NOT NULL,
  `owner` tinyint NOT NULL,
  `latitude` tinyint NOT NULL,
  `longitude` tinyint NOT NULL,
  `territory_id` tinyint NOT NULL,
  `revenue` tinyint NOT NULL,
  `purchasing_date` tinyint NOT NULL,
  `purchasing_price` tinyint NOT NULL,
  `sale_price` tinyint NOT NULL,
  `special` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `leaderboard_view`
--

DROP TABLE IF EXISTS `leaderboard_view`;
/*!50001 DROP VIEW IF EXISTS `leaderboard_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `leaderboard_view` (
  `user_id` tinyint NOT NULL,
  `revenue` tinyint NOT NULL,
  `nb_territories` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3346 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player` (
  `user_id` bigint(20) unsigned NOT NULL,
  `identifier` binary(20) NOT NULL,
  `current_money` int(10) NOT NULL,
  `total_gain` int(10) unsigned NOT NULL,
  `experience_points` int(10) unsigned NOT NULL,
  `home` int(10) unsigned NOT NULL,
  `purchase_price_skill_level` int(10) unsigned NOT NULL,
  `purchase_distance_skill_level` int(10) unsigned NOT NULL,
  `experience_limit_skill_level` int(10) unsigned NOT NULL,
  `money_limit_skill_level` int(10) unsigned NOT NULL,
  `experience_quantity_found_skill_level` int(10) unsigned NOT NULL,
  `alliance_price_skill_level` int(10) unsigned NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `userid_player` (`identifier`),
  KEY `idx_player` (`home`),
  CONSTRAINT `fk_player_home_location` FOREIGN KEY (`home`) REFERENCES `location` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `territory`
--

DROP TABLE IF EXISTS `territory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `territory` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `owner` bigint(20) unsigned NOT NULL,
  `location` int(10) unsigned NOT NULL,
  `revenue` int(10) unsigned NOT NULL,
  `purchasing_date` datetime DEFAULT NULL,
  `purchasing_price` int(10) unsigned DEFAULT NULL,
  `sale_price` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_territory` (`location`),
  KEY `idx_territory_0` (`owner`),
  CONSTRAINT `fk_territory_location` FOREIGN KEY (`location`) REFERENCES `location` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_territory_owner` FOREIGN KEY (`owner`) REFERENCES `player` (`user_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4810 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trip`
--

DROP TABLE IF EXISTS `trip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trip` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `player` bigint(20) unsigned NOT NULL,
  `distance` int(10) unsigned NOT NULL,
  `secondes` int(10) unsigned NOT NULL,
  `money_earned` int(10) unsigned NOT NULL,
  `experience_earned` int(10) unsigned NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_trip` (`player`),
  CONSTRAINT `fk_trip_player` FOREIGN KEY (`player`) REFERENCES `player` (`user_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'corporations'
--
/*!50106 SET @save_time_zone= @@TIME_ZONE */ ;
/*!50106 DROP EVENT IF EXISTS `update_players_money_event` */;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8 */ ;;
/*!50003 SET character_set_results = utf8 */ ;;
/*!50003 SET collation_connection  = utf8_general_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = '' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE EVENT `update_players_money_event` ON SCHEDULE EVERY 1 HOUR STARTS '2013-12-01 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO call update_players_money() */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
DELIMITER ;
/*!50106 SET TIME_ZONE= @save_time_zone */ ;

--
-- Dumping routines for database 'corporations'
--
/*!50003 DROP PROCEDURE IF EXISTS `capture_special_territory` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `capture_special_territory`(IN input_identifier CHAR(40), IN input_latitude double, IN input_longitude double, IN input_owner BIGINT unsigned, OUT output_result TINYINT(1))
BEGIN
	DECLARE current_owner BIGINT unsigned;
	DECLARE is_special int(1);
	DECLARE current_territory_id int(10)unsigned;
	SET @delta = 0.001;

	CALL check_identifier(input_identifier, @user_id, @result);

	SELECT owner, special, territory_id INTO current_owner, is_special, current_territory_id FROM detailed_territory WHERE latitude BETWEEN input_latitude - @delta AND input_latitude + @delta AND longitude BETWEEN input_longitude - @delta AND input_longitude + @delta;

	IF @result = 0 THEN
		IF current_owner = input_owner AND is_special = 1 THEN
			UPDATE territory SET owner = @user_id, purchasing_date = now() WHERE id = current_territory_id;
		ELSEIF current_owner != input_owner AND is_special = 1 THEN
			SET @result = 5; # OWNER_CHANGE
		ELSE
			SET @result = 4; # UNKNOWN_ERROR
		END IF;
	END IF;

	SET output_result = @result;
	SELECT dt.owner AS o, bit_or(ifnull(a.solicitor = @user_id, false)) AS a, dt.revenue AS r, dt.latitude AS la, dt.longitude AS lo, TIMESTAMPDIFF(HOUR, dt.purchasing_date, now()) AS t FROM detailed_territory dt
			LEFT JOIN alliance a ON (dt.owner = a.solicitee) WHERE dt.latitude BETWEEN input_latitude - @delta AND input_latitude + @delta AND dt.longitude BETWEEN input_longitude - @delta AND input_longitude + @delta;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `change_territory_price` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `change_territory_price`(IN input_identifier CHAR(40), IN input_new_price INT(10)UNSIGNED, IN input_latitude double, IN input_longitude double, OUT output_result TINYINT(1))
BEGIN
	DECLARE current_owner BIGINT unsigned;
	DECLARE is_special int(1);
	DECLARE current_territory_id int(10)unsigned;
	SET @delta = 0.001;

	CALL check_identifier(input_identifier, @user_id, @result);
	IF @result = 0 THEN # OK
		SELECT owner, special, territory_id INTO current_owner, is_special, current_territory_id FROM detailed_territory WHERE latitude BETWEEN input_latitude - @delta AND input_latitude + @delta AND longitude BETWEEN input_longitude - @delta AND input_longitude + @delta;
		IF current_owner IS NOT NULL AND is_special != 1 AND current_owner = @user_id THEN
			UPDATE territory SET sale_price = input_new_price WHERE id = current_territory_id;
		ELSE
		SET @result = 4; # UNKNOWN_ERROR
		END IF;
	ELSE
		SET @result = 1; # UNKNOW_IDENTIFIER
	END IF;
	SET output_result = @result;
	SELECT dt.owner AS o, bit_or(ifnull(a.solicitor = @user_id, false)) AS a, dt.sale_price AS sp, dt.purchasing_price AS pp, TIMESTAMPDIFF(HOUR, dt.purchasing_date, now()) AS t, dt.revenue AS r, dt.latitude AS la, dt.longitude AS lo FROM detailed_territory dt
			LEFT JOIN alliance a ON (dt.owner = a.solicitee) WHERE dt.latitude BETWEEN input_latitude - @delta AND input_latitude + @delta AND dt.longitude BETWEEN input_longitude - @delta AND input_longitude + @delta;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `check_identifier` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `check_identifier`(IN input_identifier CHAR(40), OUT output_user_id BIGINT unsigned, OUT output_result TINYINT(1))
BEGIN
	# output_result contains 0 if identifier match, 1 otherwise.
	SELECT user_id INTO output_user_id FROM player WHERE identifier = UNHEX(input_identifier);
	IF output_user_id IS NOT null THEN
		SET output_result = 0;
	ELSE
		SET output_result = 1;
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `create_or_update_player` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `create_or_update_player`(IN input_identifier CHAR(40), IN input_user_id BIGINT unsigned, IN input_home_latitude double, IN input_home_longitude double, OUT output_result TINYINT(1))
BEGIN
	DECLARE current_identifier binary(20);
	SET output_result = 4; # UNKNOWN_ERROR

	CALL check_identifier(input_identifier, @user_id, @result);
	
	SELECT identifier INTO current_identifier FROM player WHERE user_id = input_user_id;
	IF UNHEX(input_identifier) = current_identifier THEN
		SET output_result = 0; # Nothing to update
	ELSEIF current_identifier IS NULL THEN
		INSERT INTO location (latitude, longitude) VALUES (input_home_latitude, input_home_longitude);
		INSERT INTO player (user_id, identifier, current_money, total_gain, experience_points, home, purchase_price_skill_level, purchase_distance_skill_level, experience_limit_skill_level, money_limit_skill_level, experience_quantity_found_skill_level, alliance_price_skill_level)
			VALUES (input_user_id, UNHEX(input_identifier), 10000, 0, 10, LAST_INSERT_ID(), 1, 1, 1, 1, 1, 1);
		SET output_result = 0; # All work
	ELSE
		UPDATE player SET identifier = UNHEX(input_identifier) WHERE user_id = input_user_id;
		SET output_result = 0; # Updated
	END IF;
	CALL profile_info(input_identifier, @result);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `leaderboard` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `leaderboard`(IN input_identifier CHAR(40), IN input_start INT(10)unsigned, IN input_limit INT(10)unsigned, OUT output_result TINYINT(1))
BEGIN
	CALL check_identifier(input_identifier, @user_id, @result);
	IF @result = 0 THEN
		SET @rank = 0;
		SELECT @rank:=@rank+1 AS r, l.user_id AS id, l.nb_territories AS nt, l.ally IS NOT NULL AS a FROM (SELECT user_id, count(t.revenue) AS nb_territories, a.solicitee AS ally 
			FROM (player p
				JOIN territory t ON(t.owner = p.user_id)
				LEFT JOIN alliance a ON (a.solicitor = @user_id AND a.solicitee = p.user_id))
			GROUP BY p.user_id
			ORDER BY nb_territories DESC) l;# LIMIT input_start, input_limit;
	END IF;
	SET output_result = @result;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `profile_info` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `profile_info`(IN input_identifier CHAR(40), OUT output_result TINYINT(1))
BEGIN
	CALL check_identifier(input_identifier, @user_id, @result);
	CALL rank_for_user_id(@user_id, @rank);
	IF @result = 0 THEN
		SELECT p.user_id AS id, ifnull(ally, 0) AS na, nb_territories AS nt,  ifnull(@rank, 0) AS r, ifnull(total_money_earned, 0) AS tme, current_money AS cm, revenue as cr, total_gain AS tg, experience_points AS ep, latitude AS hlat, longitude AS hlng, purchase_price_skill_level AS ppl, purchase_distance_skill_level AS pdl, experience_limit_skill_level AS ell, money_limit_skill_level AS mll, experience_quantity_found_skill_level AS eqfl, alliance_price_skill_level AS apl
			FROM player p
				join location l ON (l.id = p.home)
				inner join leaderboard_view ON (leaderboard_view.user_id = @user_id)
				left join (
					SELECT player, sum(money_earned) AS total_money_earned
						FROM trip
							WHERE trip.player = @user_id) AS trip ON (player = @user_id)
				left join (
					SELECT solicitor, count(*) AS ally 
						FROM alliance
							WHERE alliance.solicitor = @user_id) AS alliance ON (solicitor = @user_id)
		WHERE p.user_id = @user_id GROUP BY p.user_id;
	END IF;
	SET output_result = @result;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `purchase_territory` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `purchase_territory`(IN input_identifier CHAR(40), IN input_latitude double, IN input_longitude double, IN input_owner BIGINT unsigned, IN input_price int(10)unsigned, OUT output_result TINYINT(1))
BEGIN
	DECLARE current_owner BIGINT unsigned;
	DECLARE is_special int(1);
	DECLARE current_territory_id int(10)unsigned;
	DECLARE current_sale_price int(10)unsigned;
	DECLARE current_player_money int(10)unsigned;
	DECLARE current_old_player_money int(10)unsigned;
	SET @delta = 0.001;

	CALL check_identifier(input_identifier, @user_id, @result);
	IF @result = 0 THEN # OK
		SELECT owner, sale_price, special, territory_id INTO current_owner, current_sale_price, is_special, current_territory_id FROM detailed_territory WHERE latitude BETWEEN input_latitude - @delta AND input_latitude + @delta AND longitude BETWEEN input_longitude - @delta AND input_longitude + @delta;
		SELECT current_money INTO current_player_money FROM player WHERE user_id = @user_id;
		IF current_owner IS NOT NULL AND is_special != 1 THEN
			IF current_owner = input_owner THEN # None empty territory
				IF current_sale_price > 0 AND current_sale_price = input_price THEN
					IF current_player_money >= current_sale_price THEN
						UPDATE territory SET owner = @user_id, sale_price = 0, purchasing_price = current_sale_price, purchasing_date = NOW() WHERE id = current_territory_id;
						SELECT current_money INTO current_old_player_money FROM player WHERE user_id = input_owner;
						UPDATE player SET current_money = (current_player_money - current_sale_price) WHERE user_id = @user_id;
						UPDATE player SET current_money = (current_old_player_money + current_sale_price) WHERE user_id = input_owner;
					ELSE
						SET @result = 6; # NOT_ENOUGTH_MONEY
					END IF;
				ELSE
					SET @result = 7; # PRICE_CHANGE
				END IF;
			ELSE
				SET @result = 5; # OWNER_CHANGE
			END IF;
		ELSEIF (is_special IS NULL OR is_special != 1) AND input_price > 0 THEN # Empty territory
			IF current_player_money >= input_price THEN
				INSERT INTO location (latitude, longitude) VALUES (input_latitude, input_longitude);
				INSERT INTO territory (owner, location, revenue, purchasing_date, purchasing_price, sale_price) VALUES (@user_id, LAST_INSERT_ID(), (400 + ROUND(RAND()*601))*10, NOW(), input_price, 0);
				UPDATE player SET current_money = (current_player_money - input_price) WHERE user_id = @user_id;
			ELSE
				SET @result = 6; # NOT_ENOUGTH_MONEY
			END IF;
		ELSE
			SET @result = 4; # UNKNOWN_ERROR
		END IF;
	END IF;
	SET output_result = @result;
	SELECT dt.owner AS o, bit_or(ifnull(a.solicitor = @user_id, false)) AS a, dt.sale_price AS sp, dt.purchasing_price AS pp, TIMESTAMPDIFF(HOUR, dt.purchasing_date, now()) AS t, dt.revenue AS r, dt.latitude AS la, dt.longitude AS lo FROM detailed_territory dt
			LEFT JOIN alliance a ON (dt.owner = a.solicitee) WHERE dt.latitude BETWEEN input_latitude - @delta AND input_latitude + @delta AND dt.longitude BETWEEN input_longitude - @delta AND input_longitude + @delta;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `rank_for_user_id` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `rank_for_user_id`(IN inupt_user_id BIGINT UNSIGNED, OUT output_rank INT(10)UNSIGNED)
BEGIN
	SET @rank = 0;
	SELECT rank into output_rank FROM 
		(SELECT user_id, @rank:= @rank + 1 AS rank FROM 
			(player JOIN territory ON (owner = user_id))
				GROUP BY user_id ORDER by count(territory.revenue) desc) l WHERE l.user_id = inupt_user_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `territories_for_location` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `territories_for_location`(IN input_identifier CHAR(40), IN input_latitude double, IN input_longitude double, IN input_limit INT(10)UNSIGNED, OUT output_result TINYINT(1))
BEGIN
CALL check_identifier(input_identifier, @user_id, @result);
IF @result = 0 THEN
	SELECT dt.owner AS o, bit_or(ifnull(a.solicitor = @user_id, false)) AS a, dt.revenue AS r, dt.special AS s, ifnull(dt.purchasing_price, 0) AS pp, ifnull(dt.sale_price, 0) AS sp, TIMESTAMPDIFF(HOUR, dt.purchasing_date, now()) AS t, dt.latitude AS la, dt.longitude AS lo FROM corporations.detailed_territory dt
		LEFT JOIN corporations.alliance a ON (dt.owner = a.solicitee)
	GROUP BY dt.id
	ORDER BY sqrt(pow(abs(dt.latitude - input_latitude), 2)+pow(abs(dt.longitude - input_longitude), 2)); #  LIMIT 0, 42
END IF;
SET output_result = @result;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `trips` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `trips`(IN input_identifier CHAR(40), IN input_limit INT(10)UNSIGNED, OUT output_result TINYINT(1))
BEGIN
	CALL check_identifier(input_identifier, @user_id, @result);
	IF @result = 0 THEN
			SELECT date AS da, distance AS d, secondes AS t, money_earned AS m, experience_earned AS e FROM trip WHERE player = @user_id;# LIMIT0, input_limit;
		END IF;
		SET output_result = @result;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_alliance` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `update_alliance`(IN input_identifier CHAR(40), IN input_ally BIGINT unsigned, IN create_or_delete TINYINT(1), OUT output_result TINYINT(1))
BEGIN

	CALL check_identifier(input_identifier, @user_id, @result);
	SELECT count(*) INTO @already_exists FROM alliance WHERE solicitor = @user_id AND solicitee = input_ally;

	IF create_or_delete = 1 THEN # create
		IF @already_exists = 0 THEN
			INSERT INTO alliance (solicitor, solicitee) VALUES (@user_id,input_ally);
			SET @result = 0;
		ELSE
			SET @result = 8; # ALREADY_EXISTS
		END IF;
	ELSE # delete
		IF @already_exists = 1 THEN
			DELETE FROM alliance WHERE solicitor = @user_id AND solicitee = input_ally;
			SET @result = 0;
		ELSE
			SET @result = 9; # DONT_EXISTS
		END IF;
	END IF;
	set output_result = @result;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_players_money` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `update_players_money`()
BEGIN
	UPDATE player 
		SET 
			current_money = current_money + (SELECT 
					revenue
				FROM
					leaderboard_view
				WHERE
					leaderboard_view.user_id = player.user_id),
			total_gain = total_gain + (SELECT 
					revenue
				FROM
					leaderboard_view
				WHERE
					leaderboard_view.user_id = player.user_id)
		WHERE
			user_id >= 0; # needed for safe request
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_profile` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `update_profile`(IN input_identifier CHAR(40), IN input_experience_points_price INT(10)UNSIGNED, IN input_purchase_price_skill_level INT(10)UNSIGNED, IN input_purchase_distance_skill_level INT(10)UNSIGNED, IN input_experience_limit_skill_level INT(10)UNSIGNED, IN input_money_limit_skill_level INT(10)UNSIGNED, IN input_experience_quantity_found_skill_level INT(10)UNSIGNED, IN input_alliance_price_skill_level INT(10)UNSIGNED, OUT output_result TINYINT(1))
BEGIN
	DECLARE correct_input tinyint(1)unsigned;
	DECLARE current_experience_points int(10)unsigned;

	SET @max_level = 50;

	CALL check_identifier(input_identifier, @user_id, @result);

	SELECT 
		experience_points, count(*) = 1
	INTO current_experience_points, correct_input FROM
		player
	WHERE user_id = @user_id
		AND purchase_price_skill_level <= input_purchase_price_skill_level
		AND purchase_distance_skill_level <= input_purchase_distance_skill_level
		AND experience_limit_skill_level <= input_experience_limit_skill_level
		AND money_limit_skill_level <= input_money_limit_skill_level
		AND experience_quantity_found_skill_level <= input_experience_quantity_found_skill_level
		AND alliance_price_skill_level <= input_alliance_price_skill_level;

	IF @result = 0 THEN # OK
		IF correct_input != 0 AND current_experience_points >= input_experience_points_price THEN # We can update
			IF input_purchase_price_skill_level <= @max_level AND input_purchase_distance_skill_level <= @max_level AND input_experience_limit_skill_level <= @max_level AND input_money_limit_skill_level <= @max_level AND input_experience_quantity_found_skill_level <= @max_level AND input_alliance_price_skill_level <= @max_level THEN # We can update
				UPDATE player 
				SET 
					experience_points = current_experience_points - input_experience_points_price,
					purchase_price_skill_level = input_purchase_price_skill_level,
					purchase_distance_skill_level = input_purchase_distance_skill_level,
					experience_limit_skill_level = input_experience_limit_skill_level,
					money_limit_skill_level = input_money_limit_skill_level,
					experience_quantity_found_skill_level = input_experience_quantity_found_skill_level,
					alliance_price_skill_level = input_alliance_price_skill_level
				WHERE
					user_id = @user_id;
			ELSE
				set output_result = 3; # INVALID_PARAMETER
			END IF;
		ELSE
			set output_result = 3; # INVALID_PARAMETER
		END IF;
	END IF;
	call profile_info(input_identifier, @result);	
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `upload_trip` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `upload_trip`(IN input_identifier CHAR(40), IN input_distance INT(10)UNSIGNED, IN input_secondes INT(10)UNSIGNED, IN input_date DATETIME, OUT output_result TINYINT(1))
BEGIN
	DECLARE current_money_limit_skill_level int(10)unsigned;
	DECLARE current_experience_limit_skill_level int(10)unsigned;
	DECLARE current_experience_quantity_found_skill_level int(10)unsigned;

	CALL check_identifier(input_identifier, @user_id, @result);

	SELECT money_limit_skill_level, experience_limit_skill_level, experience_quantity_found_skill_level INTO current_money_limit_skill_level, current_experience_limit_skill_level, current_experience_quantity_found_skill_level FROM player WHERE user_id = @user_id;

	SET @money_earned = input_secondes / 10 + input_distance * 10;
	SET @experience_earned = (input_distance / 10000) * 2 * current_experience_quantity_found_skill_level;

	IF @money_earned > (current_money_limit_skill_level * 10000) THEN
		SET @money_earned = current_money_limit_skill_level * 10000;
	END IF;

	IF @experience_earned > (current_experience_limit_skill_level * 25) THEN
		SET @experience_earned = current_experience_limit_skill_level * 25;
	END IF;

	IF @result = 0 THEN
		INSERT INTO trip (player, distance, secondes, money_earned, experience_earned, date)
		VALUES (@user_id, input_distance, input_secondes, @money_earned, @experience_earned, input_date);
		SELECT experience_points INTO @current_experience_points FROM player WHERE user_id = @user_id;
		UPDATE player SET experience_points = @current_experience_points + @experience_earned WHERE user_id = @user_id;
	END IF;
	SET output_result = @result;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `alliances`
--

/*!50001 DROP TABLE IF EXISTS `alliances`*/;
/*!50001 DROP VIEW IF EXISTS `alliances`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 SQL SECURITY DEFINER */
/*!50001 VIEW `alliances` AS select `player`.`user_id` AS `user_id`,count(`alliance`.`solicitor`) AS `nb_alliance` from (`player` join `alliance` on((`alliance`.`solicitor` = `player`.`user_id`))) group by `player`.`user_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `allies`
--

/*!50001 DROP TABLE IF EXISTS `allies`*/;
/*!50001 DROP VIEW IF EXISTS `allies`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 SQL SECURITY DEFINER */
/*!50001 VIEW `allies` AS select `player`.`user_id` AS `user_id`,count(`alliance`.`solicitee`) AS `nb_alliance` from (`player` join `alliance` on((`alliance`.`solicitee` = `player`.`user_id`))) group by `player`.`user_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `detailed_territory`
--

/*!50001 DROP TABLE IF EXISTS `detailed_territory`*/;
/*!50001 DROP VIEW IF EXISTS `detailed_territory`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 SQL SECURITY DEFINER */
/*!50001 VIEW `detailed_territory` AS select `t`.`id` AS `id`,`p`.`user_id` AS `owner`,`l`.`latitude` AS `latitude`,`l`.`longitude` AS `longitude`,`t`.`id` AS `territory_id`,`t`.`revenue` AS `revenue`,`t`.`purchasing_date` AS `purchasing_date`,`t`.`purchasing_price` AS `purchasing_price`,`t`.`sale_price` AS `sale_price`,isnull(`t`.`purchasing_price`) AS `special` from ((`player` `p` join `territory` `t` on((`p`.`user_id` = `t`.`owner`))) join `location` `l` on((`t`.`location` = `l`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `leaderboard_view`
--

/*!50001 DROP TABLE IF EXISTS `leaderboard_view`*/;
/*!50001 DROP VIEW IF EXISTS `leaderboard_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 SQL SECURITY DEFINER */
/*!50001 VIEW `leaderboard_view` AS select `player`.`user_id` AS `user_id`,ifnull(round(((sum(`territory`.`revenue`) - ifnull(((`alliances`.`nb_alliance` * 10000) * (1.0 - (`player`.`alliance_price_skill_level` * 0.015))),0)) + ifnull((`allies`.`nb_alliance` * 10000),0)),0),0) AS `revenue`,count(`territory`.`revenue`) AS `nb_territories` from (((`player` left join `territory` on((`territory`.`owner` = `player`.`user_id`))) left join `allies` on((`allies`.`user_id` = `player`.`user_id`))) left join `alliances` on((`alliances`.`user_id` = `player`.`user_id`))) group by `player`.`user_id` order by count(`territory`.`revenue`) desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-01-24 14:54:30
