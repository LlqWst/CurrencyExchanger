CREATE TABLE IF NOT EXISTS Currencies
(
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Code VARCHAR(3) NOT NULL,
    FullName VARCHAR(46) NOT NULL,
    Sign VARCHAR(5) NOT NULL,
    CONSTRAINT currencies_unique_code UNIQUE (Code)
);

CREATE TABLE IF NOT EXISTS ExchangeRates
(
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    BaseCurrencyId INTEGER NOT NULL,
    TargetCurrencyId INTEGER NOT NULL,
    Rate INTEGER NOT NULL CHECK (Rate >= 0 AND Rate <= 999999999999),
    FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies (ID) ON DELETE RESTRICT,
    FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies (ID) ON DELETE RESTRICT,
    CHECK (BaseCurrencyId != TargetCurrencyId),
    CONSTRAINT exchange_rates_unique_currency_ids UNIQUE (BaseCurrencyId, TargetCurrencyId)
);