CREATE TABLE cardholder (
  id int auto_increment PRIMARY KEY,
  ssn varchar(11),
  name varchar(255),
  UNIQUE KEY unique_ssn(ssn));

ALTER TABLE account ADD cardholder_id INT NOT NULL;
ALTER TABLE account ADD CONSTRAINT account_cardholder_id FOREIGN KEY(cardholder_id) REFERENCES cardholder(id);

ALTER TABLE account ADD customer_id INT NOT NULL;
ALTER TABLE account ADD CONSTRAINT account_customer_id FOREIGN KEY(customer_id) REFERENCES customer(id);

ALTER TABLE account ADD UNIQUE unique_customer_cardholder(customer_id, cardholder_id);

ALTER TABLE customer ADD CONSTRAINT unique_customer_name UNIQUE (name);
