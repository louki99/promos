-- =====================================================
-- FoodPlus POS System - Initial Data Seeding
-- =====================================================

-- Insert Stores
INSERT INTO stores (name, code, address, city, phone, email, tax_number, active) VALUES
('FoodPlus Centre-Ville', 'STORE001', '123 Rue de la Paix', 'Casablanca', '+212-5-22-123456', 'store1@foodplus.com', 'TAX001', true),
('FoodPlus Mall', 'STORE002', '456 Avenue Mohammed V', 'Rabat', '+212-5-37-789012', 'store2@foodplus.com', 'TAX002', true),
('FoodPlus Express', 'STORE003', '789 Boulevard Hassan II', 'Marrakech', '+212-5-24-345678', 'store3@foodplus.com', 'TAX003', true),
('FoodPlus Premium', 'STORE004', '321 Rue Atlas', 'Fès', '+212-5-35-901234', 'store4@foodplus.com', 'TAX004', true);

-- Insert Taxes
INSERT INTO taxes (name, rate, description, active) VALUES
('TVA Standard', 20.00, 'Taxe sur la valeur ajoutée standard', true),
('TVA Réduite', 10.00, 'Taxe sur la valeur ajoutée réduite', true),
('TVA Super Réduite', 5.50, 'Taxe sur la valeur ajoutée super réduite', true),
('Exonéré', 0.00, 'Produits exonérés de TVA', true),
('TVA Export', 0.00, 'TVA pour export', true);

-- Insert Categories
INSERT INTO categories (name, description, code, active) VALUES
('Fruits et Légumes', 'Fruits et légumes frais', 'FRUITS_LEGUMES', true),
('Viandes', 'Viandes et volailles', 'VIANDES', true),
('Poissons', 'Poissons et fruits de mer', 'POISSONS', true),
('Boulangerie', 'Pain et pâtisseries', 'BOULANGERIE', true),
('Épicerie', 'Produits d''épicerie', 'EPICERIE', true),
('Boissons', 'Boissons et jus', 'BOISSONS', true),
('Produits Laitiers', 'Lait et produits laitiers', 'LAITIERS', true),
('Hygiène', 'Produits d''hygiène', 'HYGIENE', true),
('Entretien', 'Produits d''entretien', 'ENTRETIEN', true),
('Électronique', 'Produits électroniques', 'ELECTRONIQUE', true);

-- Insert Product Clusters
INSERT INTO product_cluster (code, name, description) VALUES
('CLUSTER001', 'Produits Frais', 'Produits frais et périssables'),
('CLUSTER002', 'Produits Secs', 'Produits secs et conserves'),
('CLUSTER003', 'Produits Surgelés', 'Produits surgelés'),
('CLUSTER004', 'Produits Bio', 'Produits biologiques'),
('CLUSTER005', 'Produits Premium', 'Produits haut de gamme');

-- Insert Products
INSERT INTO products (name, description, sku, barcode, category_id, cost_price, selling_price, wholesale_price, tax_id, unit, min_stock_level, max_stock_level, track_inventory, active, product_cluster_id) VALUES
-- Fruits et Légumes (TVA Réduite 10%)
('Pommes Golden', 'Pommes golden fraîches', 'PROD001', '1234567890001', 1, 2.50, 3.99, 3.50, 2, 'kg', 10, 100, true, true, 1),
('Bananes', 'Bananes fraîches', 'PROD002', '1234567890002', 1, 1.80, 2.99, 2.50, 2, 'kg', 15, 150, true, true, 1),
('Tomates', 'Tomates fraîches', 'PROD003', '1234567890003', 1, 1.20, 2.49, 2.00, 2, 'kg', 20, 200, true, true, 1),
('Carottes', 'Carottes fraîches', 'PROD004', '1234567890004', 1, 0.80, 1.99, 1.50, 2, 'kg', 25, 250, true, true, 1),

-- Viandes (TVA Standard 20%)
('Poulet Entier', 'Poulet entier frais', 'PROD005', '1234567890005', 2, 8.50, 12.99, 11.50, 1, 'unité', 5, 50, true, true, 1),
('Bœuf Haché', 'Bœuf haché frais', 'PROD006', '1234567890006', 2, 12.00, 18.99, 16.50, 1, 'kg', 8, 80, true, true, 1),
('Agneau Côtelettes', 'Côtelettes d''agneau', 'PROD007', '1234567890007', 2, 15.00, 24.99, 22.00, 1, 'kg', 3, 30, true, true, 1),

-- Boulangerie (TVA Réduite 10%)
('Pain Baguette', 'Pain baguette tradition', 'PROD008', '1234567890008', 4, 0.30, 0.99, 0.80, 2, 'unité', 50, 500, true, true, 1),
('Croissants', 'Croissants au beurre', 'PROD009', '1234567890009', 4, 0.50, 1.49, 1.20, 2, 'unité', 30, 300, true, true, 1),
('Pain Complet', 'Pain complet bio', 'PROD010', '1234567890010', 4, 0.80, 2.99, 2.50, 2, 'unité', 20, 200, true, true, 1),

-- Épicerie (TVA Standard 20%)
('Riz Basmati', 'Riz basmati premium', 'PROD011', '1234567890011', 5, 2.50, 4.99, 4.00, 1, 'kg', 40, 400, true, true, 2),
('Huile d''Olive', 'Huile d''olive extra vierge', 'PROD012', '1234567890012', 5, 8.00, 15.99, 13.50, 1, 'litre', 15, 150, true, true, 2),
('Pâtes Spaghetti', 'Spaghetti de qualité', 'PROD013', '1234567890013', 5, 1.20, 2.49, 2.00, 1, 'kg', 60, 600, true, true, 2),
('Sucre Blanc', 'Sucre blanc raffiné', 'PROD014', '1234567890014', 5, 0.80, 1.99, 1.50, 1, 'kg', 80, 800, true, true, 2),

-- Boissons (TVA Standard 20%)
('Eau Minérale', 'Eau minérale naturelle', 'PROD015', '1234567890015', 6, 0.20, 0.99, 0.80, 1, 'litre', 100, 1000, true, true, 2),
('Jus d''Orange', 'Jus d''orange frais', 'PROD016', '1234567890016', 6, 1.50, 3.99, 3.20, 1, 'litre', 25, 250, true, true, 2),
('Coca-Cola', 'Coca-Cola classique', 'PROD017', '1234567890017', 6, 0.40, 1.49, 1.20, 1, 'litre', 50, 500, true, true, 2),
('Thé Vert', 'Thé vert bio', 'PROD018', '1234567890018', 6, 2.00, 4.99, 4.00, 1, 'boîte', 20, 200, true, true, 2);

-- Insert Terminals
INSERT INTO terminals (name, code, store_id, type, location, active) VALUES
-- Store 1 (Casablanca) - 4 terminals
('Caisse 1', 'TERM001', 1, 'CASH_REGISTER', 'Entrée principale', true),
('Caisse 2', 'TERM002', 1, 'CASH_REGISTER', 'Zone fruits et légumes', true),
('Kiosque Libre-Service', 'TERM003', 1, 'SELF_CHECKOUT', 'Zone libre-service', true),
('Terminal Mobile', 'TERM004', 1, 'MOBILE_POS', 'Zone mobile', true),

-- Store 2 (Rabat) - 4 terminals
('Caisse 1', 'TERM005', 2, 'CASH_REGISTER', 'Entrée principale', true),
('Caisse 2', 'TERM006', 2, 'CASH_REGISTER', 'Zone boulangerie', true),
('Drive-Thru', 'TERM007', 2, 'DRIVE_THRU', 'Drive-thru', true),
('Terminal Admin', 'TERM008', 2, 'ADMIN_TERMINAL', 'Bureau manager', true),

-- Store 3 (Marrakech) - 4 terminals
('Caisse 1', 'TERM009', 3, 'CASH_REGISTER', 'Entrée principale', true),
('Caisse 2', 'TERM010', 3, 'CASH_REGISTER', 'Zone viandes', true),
('Kiosque', 'TERM011', 3, 'KIOSK', 'Zone kiosque', true),
('Terminal Livraison', 'TERM012', 3, 'DELIVERY_POS', 'Zone livraison', true),

-- Store 4 (Fès) - 4 terminals
('Caisse 1', 'TERM013', 4, 'CASH_REGISTER', 'Entrée principale', true),
('Caisse 2', 'TERM014', 4, 'CASH_REGISTER', 'Zone épicerie', true),
('Terminal Mobile', 'TERM015', 4, 'MOBILE_POS', 'Zone mobile', true),
('Terminal Admin', 'TERM016', 4, 'ADMIN_TERMINAL', 'Bureau manager', true);

-- Insert Users (passwords are BCrypt hashed)
-- Default password for all users: 'password123' (BCrypt hash: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa)
INSERT INTO users (first_name, last_name, email, username, password, role, store_id, active, phone) VALUES
-- Admin users (no store assignment)
('System', 'Administrator', 'admin@foodplus.com', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', NULL, true, '+212-6-00-000000'),
('Super', 'Admin', 'superadmin@foodplus.com', 'superadmin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', NULL, true, '+212-6-00-000000'),

-- Store 1 (Casablanca) - Manager and Staff
('Ahmed', 'Benali', 'ahmed.benali@foodplus.com', 'manager1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'MANAGER', 1, true, '+212-6-00-000000'),
('Fatima', 'Zahra', 'fatima.zahra@foodplus.com', 'cashier1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'CASHIER', 1, true, '+212-6-00-000000'),
('Karim', 'Tazi', 'karim.tazi@foodplus.com', 'supervisor1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'SUPERVISOR', 1, true, '+212-6-00-000000'),
('Amina', 'El Fassi', 'amina.elfassi@foodplus.com', 'inventory1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'INVENTORY_MANAGER', 1, true, '+212-6-00-000000'),

-- Store 2 (Rabat) - Manager and Staff
('Hassan', 'Mekki', 'hassan.mekki@foodplus.com', 'manager2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'MANAGER', 2, true, '+212-6-00-000000'),
('Leila', 'Bennani', 'leila.bennani@foodplus.com', 'cashier2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'CASHIER', 2, true, '+212-6-00-000000'),
('Omar', 'Cherkaoui', 'omar.cherkaoui@foodplus.com', 'supervisor2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'SUPERVISOR', 2, true, '+212-6-00-000000'),
('Nadia', 'Rahmani', 'nadia.rahmani@foodplus.com', 'inventory2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'INVENTORY_MANAGER', 2, true, '+212-6-00-000000'),

-- Store 3 (Marrakech) - Manager and Staff
('Youssef', 'Alaoui', 'youssef.alaoui@foodplus.com', 'manager3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'MANAGER', 3, true, '+212-6-00-000000'),
('Sara', 'Bouazza', 'sara.bouazza@foodplus.com', 'cashier3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'CASHIER', 3, true, '+212-6-00-000000'),
('Mehdi', 'Lahlou', 'mehdi.lahlou@foodplus.com', 'supervisor3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'SUPERVISOR', 3, true, '+212-6-00-000000'),
('Hanae', 'Touil', 'hanae.touil@foodplus.com', 'inventory3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'INVENTORY_MANAGER', 3, true, '+212-6-00-000000'),

-- Store 4 (Fès) - Manager and Staff
('Rachid', 'Boujida', 'rachid.boujida@foodplus.com', 'manager4', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'MANAGER', 4, true, '+212-6-00-000000'),
('Imane', 'Sefiani', 'imane.sefiani@foodplus.com', 'cashier4', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'CASHIER', 4, true, '+212-6-00-000000'),
('Adil', 'Mansouri', 'adil.mansouri@foodplus.com', 'supervisor4', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'SUPERVISOR', 4, true, '+212-6-00-000000'),
('Salma', 'Berrada', 'salma.berrada@foodplus.com', 'inventory4', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'INVENTORY_MANAGER', 4, true, '+212-6-00-000000');

-- Insert Partners
INSERT INTO partners (first_name, last_name, email, phone, address, city, partner_type, loyalty_tier, loyalty_points, active) VALUES
-- VIP Partners (3)
('Mohammed', 'Alaoui', 'mohammed.alaoui@email.com', '+212-6-12-345678', '123 Rue Hassan II', 'Casablanca', 'VIP', 'GOLD', 1500, true),
('Karim', 'Mekki', 'karim.mekki@email.com', '+212-6-56-789012', '654 Rue de la Paix', 'Tanger', 'VIP', 'GOLD', 1200, true),
('Youssef', 'Bouazza', 'youssef.bouazza@email.com', '+212-6-90-123456', '369 Avenue Al Maghrib', 'Tétouan', 'VIP', 'GOLD', 1800, true),

-- Wholesale Partners (2)
('Hassan', 'Tazi', 'hassan.tazi@email.com', '+212-6-34-567890', '789 Boulevard Hassan II', 'Marrakech', 'WHOLESALE', 'PLATINUM', 2500, true),
('Omar', 'Rahmani', 'omar.rahmani@email.com', '+212-6-78-901234', '147 Boulevard Mohammed VI', 'Oujda', 'WHOLESALE', 'PLATINUM', 3000, true),

-- Regular Partners (5)
('Amina', 'Benjelloun', 'amina.benjelloun@email.com', '+212-6-23-456789', '456 Avenue Mohammed V', 'Rabat', 'REGULAR', 'SILVER', 750, true),
('Fatima', 'Cherkaoui', 'fatima.cherkaoui@email.com', '+212-6-45-678901', '321 Rue Atlas', 'Fès', 'REGULAR', 'BRONZE', 300, true),
('Leila', 'Bennani', 'leila.bennani@email.com', '+212-6-67-890123', '987 Avenue Ibn Sina', 'Agadir', 'REGULAR', 'SILVER', 600, true),
('Nadia', 'El Fassi', 'nadia.elfassi@email.com', '+212-6-89-012345', '258 Rue Ibn Khaldoun', 'Meknès', 'REGULAR', 'BRONZE', 200, true),
('Sara', 'Lahlou', 'sara.lahlou@email.com', '+212-6-01-234567', '741 Rue Ibn Batouta', 'Larache', 'REGULAR', 'SILVER', 450, true);

-- Insert Inventory (random stock levels between 50-200 for each product in each store)
INSERT INTO inventory (product_id, store_id, quantity, reserved_quantity) VALUES
-- Store 1 (Casablanca) - Products 1-18
(1, 1, 125, 0), (2, 1, 167, 0), (3, 1, 89, 0), (4, 1, 143, 0), (5, 1, 45, 0), (6, 1, 78, 0), (7, 1, 23, 0), (8, 1, 234, 0), (9, 1, 156, 0), (10, 1, 98, 0), (11, 1, 189, 0), (12, 1, 67, 0), (13, 1, 234, 0), (14, 1, 345, 0), (15, 1, 456, 0), (16, 1, 123, 0), (17, 1, 234, 0), (18, 1, 89, 0),

-- Store 2 (Rabat) - Products 1-18
(1, 2, 156, 0), (2, 2, 134, 0), (3, 2, 178, 0), (4, 2, 112, 0), (5, 2, 34, 0), (6, 2, 89, 0), (7, 2, 28, 0), (8, 2, 267, 0), (9, 2, 145, 0), (10, 2, 87, 0), (11, 2, 198, 0), (12, 2, 76, 0), (13, 2, 245, 0), (14, 2, 378, 0), (15, 2, 423, 0), (16, 2, 134, 0), (17, 2, 256, 0), (18, 2, 67, 0),

-- Store 3 (Marrakech) - Products 1-18
(1, 3, 145, 0), (2, 3, 189, 0), (3, 3, 134, 0), (4, 3, 167, 0), (5, 3, 56, 0), (6, 3, 98, 0), (7, 3, 34, 0), (8, 3, 289, 0), (9, 3, 167, 0), (10, 3, 76, 0), (11, 3, 212, 0), (12, 3, 89, 0), (13, 3, 278, 0), (14, 3, 412, 0), (15, 3, 389, 0), (16, 3, 145, 0), (17, 3, 278, 0), (18, 3, 78, 0),

-- Store 4 (Fès) - Products 1-18
(1, 4, 134, 0), (2, 4, 145, 0), (3, 4, 189, 0), (4, 4, 123, 0), (5, 4, 45, 0), (6, 4, 87, 0), (7, 4, 29, 0), (8, 4, 234, 0), (9, 4, 134, 0), (10, 4, 89, 0), (11, 4, 198, 0), (12, 4, 67, 0), (13, 4, 256, 0), (14, 4, 345, 0), (15, 4, 456, 0), (16, 4, 123, 0), (17, 4, 234, 0), (18, 4, 67, 0);

-- Insert some sample stock movements for demonstration
INSERT INTO stock_movements (product_id, store_id, quantity, movement_type, reference, performed_by) VALUES
(1, 1, 100, 'IN', 'Initial stock', 3),
(2, 1, 150, 'IN', 'Initial stock', 3),
(3, 1, 80, 'IN', 'Initial stock', 3),
(4, 1, 120, 'IN', 'Initial stock', 3),
(5, 1, 40, 'IN', 'Initial stock', 3),
(6, 1, 70, 'IN', 'Initial stock', 3),
(7, 1, 20, 'IN', 'Initial stock', 3),
(8, 1, 200, 'IN', 'Initial stock', 3),
(9, 1, 120, 'IN', 'Initial stock', 3),
(10, 1, 80, 'IN', 'Initial stock', 3);

-- Insert some sample shifts for demonstration
INSERT INTO shifts (terminal_id, cashier_id, store_id, start_time, status, opening_balance) VALUES
(1, 4, 1, CURRENT_TIMESTAMP - INTERVAL '2 hours', 'OPEN', 1000.00),
(5, 8, 2, CURRENT_TIMESTAMP - INTERVAL '1 hour', 'OPEN', 1500.00),
(9, 12, 3, CURRENT_TIMESTAMP - INTERVAL '30 minutes', 'OPEN', 800.00),
(13, 16, 4, CURRENT_TIMESTAMP - INTERVAL '15 minutes', 'OPEN', 1200.00);

-- Add comments for documentation
COMMENT ON TABLE stores IS 'Stores information about retail locations';
COMMENT ON TABLE users IS 'System users with different roles and permissions';
COMMENT ON TABLE terminals IS 'Point of sale terminals and devices';
COMMENT ON TABLE categories IS 'Product categories for organization';
COMMENT ON TABLE taxes IS 'Tax rates and configurations';
COMMENT ON TABLE products IS 'Products available for sale';
COMMENT ON TABLE partners IS 'Customers and business partners';
COMMENT ON TABLE inventory IS 'Product inventory levels by store';
COMMENT ON TABLE stock_movements IS 'Inventory movement tracking';
COMMENT ON TABLE shifts IS 'Cashier work shifts';

-- Log the completion
DO $$
BEGIN
    RAISE NOTICE 'FoodPlus POS System - Initial data seeding completed successfully!';
    RAISE NOTICE 'Created: 4 stores, 18 users, 16 terminals, 10 categories, 5 taxes, 18 products, 10 partners, 72 inventory records';
    RAISE NOTICE 'Default password for all users: password123';
END $$; 