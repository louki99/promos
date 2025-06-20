CREATE TABLE cat_tarif (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = NOW();
   RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_cat_tarif_updated_at
BEFORE UPDATE ON cat_tarif
FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_column();

-- dummy data insert
INSERT INTO cat_tarif (code, name, description, created_at, updated_at) VALUES
('CAT_TARI_001', 'Standard Pricing', 'Basic pricing for regular customers', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CAT_TARI_002', 'Premium Pricing', 'Higher tier pricing with extra benefits', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CAT_TARI_003', 'Wholesale Pricing', 'Discounted pricing for bulk orders', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
