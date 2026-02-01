
CREATE SCHEMA `bank_crud_example`;

USE bank_crud_example;

/* ********************************************************************************************************* */
/* ********************************************************************************************************* */

CREATE TABLE `iban_configs` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `country_code` char(2) NOT NULL,
  `check_digits` char(2) NOT NULL,
  `bank_code` char(4) NOT NULL,
  `sort_code` char(6) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `user_info` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL,
  `national_id` varchar(14) NOT NULL,
  `date_of_birth` date NOT NULL,
  `cell_phone` char(11) NOT NULL,
  `email` varchar(50) NOT NULL,
  `mailing_address` varchar(250) NOT NULL,
  `account_number` varchar(8) NOT NULL,
  `balance` float unsigned NOT NULL,
  `version` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `national_id_UQ` (`national_id`),
  UNIQUE KEY `cell_phone_UQ` (`cell_phone`),
  UNIQUE KEY `account_number_UQ` (`account_number`),
  UNIQUE KEY `email_UQ` (`email`)
);
