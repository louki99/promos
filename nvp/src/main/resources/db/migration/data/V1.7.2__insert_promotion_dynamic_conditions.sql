-- Insert promotion_dynamic_conditions
INSERT INTO promotion_dynamic_conditions (
    id, is_active, threshold, promotion_id, condition_type, condition_value,
    description, entity_id, entity_type, operator
)
VALUES 
-- Summer Sale 2024 conditions
(1, true, 100.00, 1, 'ORDER_TOTAL', '100.00',
    'Minimum order total of $100', null, null, 'GREATER_THAN_OR_EQUALS'),
(2, true, null, 1, 'PAYMENT_METHOD', 'CREDIT_CARD',
    'Only for credit card payments', null, null, 'EQUALS'),
(3, true, 3, 1, 'CUSTOMER_LOYALTY', '3',
    'Minimum loyalty level 3', null, null, 'GREATER_THAN_OR_EQUALS'),

-- Welcome Discount conditions
(4, true, null, 2, 'CUSTOMER_GROUP', 'NEW_CUSTOMER',
    'Only for new customers', null, null, 'EQUALS'),
(5, true, 30, 2, 'PURCHASE_HISTORY', '30',
    'Account age less than 30 days', null, null, 'LESS_THAN'),

-- Bulk Purchase Discount conditions
(6, true, 1000.00, 3, 'ORDER_TOTAL', '1000.00',
    'Minimum order total of $1000', null, null, 'GREATER_THAN_OR_EQUALS'),
(7, true, 5, 3, 'CUSTOMER_LOYALTY', '5',
    'Premium customers (level 5+)', null, null, 'GREATER_THAN_OR_EQUALS'),
(8, true, null, 3, 'CUSTOMER_GROUP', 'WHOLESALE',
    'Only for wholesale customers', null, null, 'EQUALS'); 