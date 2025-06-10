-- Create customer tariff table
CREATE TABLE cat_tarif (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    price_ttc DECIMAL(24,6) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
); 