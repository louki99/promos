-- Add additional indexes for better query performance
CREATE INDEX idx_products_title ON products(title);
CREATE INDEX idx_products_categories ON products(category1, category2, category3, category4);
CREATE INDEX idx_products_price ON products(sale_price, price_including_tax);
CREATE INDEX idx_products_created_at ON products(created_at);
