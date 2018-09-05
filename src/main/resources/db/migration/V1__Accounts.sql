CREATE TABLE IF NOT EXISTS accounts
(
  id          TEXT UNIQUE NOT NULL,
  balance     INT NOT NULL DEFAULT 0,
  CONSTRAINT  pk_accounts_id PRIMARY KEY (id),
  CONSTRAINT  balance_always_positive CHECK(balance >= 0)
);

CREATE TABLE IF NOT EXISTS transactions
(
  id          SERIAL,
  from_id     TEXT REFERENCES accounts(id),
  to_id       TEXT REFERENCES accounts(id),
  amount      INT DEFAULT -1,
  timestamp   BIGINT NOT NULL,
  CONSTRAINT  pk_transactions_id PRIMARY KEY (id),
  CONSTRAINT  transfer_within_amount_constraints CHECK(amount >= 0 and amount <= 999999),
  CONSTRAINT  transfer_ids_are_unique CHECK (from_id != to_id)
);

INSERT INTO accounts
(id, balance)
VALUES
('1', 100),
('2', 120),
('3', 700),
('4', 1000000);
