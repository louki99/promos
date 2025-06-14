-- Update availability_schedule and custom_attributes columns to jsonb type
ALTER TABLE products 
    ALTER COLUMN availability_schedule TYPE jsonb USING availability_schedule::jsonb,
    ALTER COLUMN custom_attributes TYPE jsonb USING custom_attributes::jsonb;

-- Add comments to explain the JSON structure
COMMENT ON COLUMN products.availability_schedule IS 'JSON structure for product availability schedule. Example: {"monday": {"start": "09:00", "end": "17:00"}, ...}';
COMMENT ON COLUMN products.custom_attributes IS 'JSON structure for custom product attributes. Example: {"storage_conditions": {"temperature": "2-8°C"}, "allergens": ["nuts", "dairy"]}'; 