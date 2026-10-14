CREATE TABLE IF NOT EXISTS categories (
	id    SERIAL PRIMARY KEY,
	name   VARCHAR(60) NOT NULL UNIQUE,
	type   VARCHAR(10) NOT NULL CHECK (type IN('INCOME','EXPENSE'))
);
CREATE TABLE IF NOT EXISTS accounts (
	id     SERIAL PRIMARY KEY,
	name   VARCHAR(60) NOT NULL,
	type   VARCHAR(10) NOT NULL DEFAULT 'CASH',
	created_at  TIMESTAMP NOT NULL DEFAULT now()
);
CREATE TABLE IF NOT EXISTS transactions (
	id    SERIAL PRIMARY KEY,
	account_id  INTEGER NOT NULL REFERENCES accounts(id),
	category_id  INTEGER NOT NULL REFERENCES categories(id),
	amount             NUMERIC(12, 2) NOT NULL CHECK (amount > 0),
    type         VARCHAR(10) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
    description  VARCHAR(255),
    occurred_on  DATE NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT now()
);
CREATE TABLE IF NOT EXISTS budgets (
	id   SERIAL PRIMARY KEY,
	category_id INTEGER NOT NULL REFERENCES categories(id) UNIQUE,
	monthly_limit NUMERIC(12,2) NOT NULL CHECK (monthly_limit>0)
);
INSERT INTO categories (name, type) VALUES
    ('Salary', 'INCOME'),
    ('Rent', 'EXPENSE'),
    ('Groceries', 'EXPENSE'),
    ('Transport', 'EXPENSE'),
    ('Entertainment', 'EXPENSE')
ON CONFLICT (name) DO NOTHING;
