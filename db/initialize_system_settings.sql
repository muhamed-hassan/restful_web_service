INSERT INTO `iban_configuration` (`country_code`, `check_digits`, `bank_code`, `sort_code`) 
    VALUES ('GB', '29', 'NWBK', '601613');

UPDATE `customer`
    SET `balance` = 15000;