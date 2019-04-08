CREATE TABLE transaction_record (
  id int auto_increment PRIMARY KEY,
  amount DOUBLE NOT NULL,
  account_id INT NOT NULL,
  date_of_transaction DATETIME,
  approved BOOLEAN,
  FOREIGN KEY(account_id) REFERENCES account(id)
) ENGINE=InnoDB;
