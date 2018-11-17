CREATE TABLE account (
  id int auto_increment PRIMARY KEY,
  credit_card_number VARCHAR(36),
  credit_limit DOUBLE,
  active BOOLEAN
) ENGINE=InnoDB;
