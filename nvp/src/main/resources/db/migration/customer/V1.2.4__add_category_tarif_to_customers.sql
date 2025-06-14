-- Add category_tarif_id column to customers table
ALTER TABLE customers
ADD COLUMN category_tarif_id BIGINT REFERENCES cat_tarif(id);

-- Add index for better query performance
CREATE INDEX idx_customers_category_tarif_id ON customers(category_tarif_id); 