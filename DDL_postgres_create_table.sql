CREATE TABLE users (
    id UUID PRIMARY KEY,
    national_idNumber VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(100),
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    gender VARCHAR(50),
    military_status BOOLEAN,
    date_of_birth DATE,
    created_date TIMESTAMP,
    phone_number VARCHAR(255),
	daily_transaction_total NUMERIC(19, 4) DEFAULT 0,
	last_transaction_date DATE,
    CONSTRAINT unique_username UNIQUE (username)
);


CREATE TABLE wallets (
    id UUID PRIMARY KEY,
    account_number NUMERIC(20, 0) NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    created_date TIMESTAMP,
    balance NUMERIC(19, 2) NOT NULL,
    iban VARCHAR(34) NOT NULL UNIQUE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    wallet_id UUID NOT NULL,
    amount NUMERIC(19, 4) NOT NULL,
    type VARCHAR(50),
    sender_id NUMERIC(20, 0) NOT NULL,
    recipient_id NUMERIC(20, 0) NOT NULL,
    description TEXT,
    created_date TIMESTAMP,
    CONSTRAINT fk_wallet FOREIGN KEY (wallet_id) REFERENCES wallets(id) ON DELETE CASCADE
);
