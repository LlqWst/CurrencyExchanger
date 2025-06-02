INSERT INTO Currencies (Code, FullName, Sign)
VALUES
    ('PLN', 'Zloty', 'zł'),
    ('USD', 'US Dollar', '$'),
    ('RUB', 'Russian Ruble', '₽'),
    ('XSU', 'Sucre', 'XSU'),
    ('EUR', 'Euro', '€');

INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate)
VALUES
    ('1', '4', '2050150'),
    ('2', '3', '235578'),
    ('4', '2', '745568'),
    ('2', '5', '1576876578');