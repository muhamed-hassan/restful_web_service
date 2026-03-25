
CREATE SCHEMA `bank_crud_example`;

USE bank_crud_example;

/* ********************************************************************************************************* */
/* ********************************************************************************************************* */

CREATE TABLE `iban_configs` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `country_code` CHAR(2) NOT NULL,
  `check_digits` CHAR(2) NOT NULL,
  `bank_code` CHAR(4) NOT NULL,
  `sort_code` CHAR(6) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `customer` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(250) NOT NULL,
  `national_id` CHAR(14) NOT NULL,
  `date_of_birth` DATE NOT NULL,
  `mobile` CHAR(11) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `mailing_address` VARCHAR(500) NOT NULL,
  `account_number` CHAR(8) NOT NULL,
  `balance` FLOAT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `national_id_UQ` (`national_id`),
  UNIQUE KEY `mobile_UQ` (`mobile`),
  UNIQUE KEY `email_UQ` (`email`),
  UNIQUE KEY `account_number_UQ` (`account_number`)
);
