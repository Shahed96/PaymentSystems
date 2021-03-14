
CREATE SCHEMA `banking_system` ;


use banking_system;


DROP TABLE IF EXISTS `initial_balances`;

CREATE TABLE `initial_balances` (
  `account_number` varchar(9) NOT NULL,
  `balance` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`account_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




LOCK TABLES `initial_balances` WRITE;

INSERT INTO `initial_balances` VALUES ('111111111',1000),('123456789',1000),('222222222',1000),('333333333',1000),('444444444',1000);

UNLOCK TABLES;



DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `debit_amount`( IN accountNumber varchar(9), IN amount Decimal)
BEGIN


	UPDATE initial_balances SET balance = balance-amount where account_number = accountNumber;

END ;;
DELIMITER ;



