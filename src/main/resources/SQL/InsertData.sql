--customer
INSERT INTO customer (name, contact_info, address) VALUES 
('John Doe', 'john@example.com', '123 Main St'),
('Jane Smith', 'jane@example.com', '456 Elm St'),
('Alice Johnson', 'alice@example.com', '789 Oak St'),
('Bob Brown', 'bob@example.com', '101 Pine St'),
('Carol White', 'carol@example.com', '202 Maple St'),
('David Green', 'david@example.com', '303 Birch St'),
('Eve Black', 'eve@example.com', '404 Cedar St'),
('Frank Wright', 'frank@example.com', '505 Spruce St'),
('Grace Harris', 'grace@example.com', '606 Willow St'),
('Henry Lewis', 'henry@example.com', '707 Fir St'),
('Ivy Walker', 'ivy@example.com', '808 Palm St'),
('Jack Robinson', 'jack@example.com', '909 Cypress St'),
('Kathy Scott', 'kathy@example.com', '1010 Redwood St'),
('Larry Turner', 'larry@example.com', '1111 Cedar St');
 
 
--delivery_partner
INSERT INTO delivery_partner (name, contact_info) VALUES 
('Fast Delivery', 'fast@example.com'),
('Quick Ship', 'quick@example.com'),
('Speedy Courier', 'speedy@example.com'),
('Reliable Logistics', 'reliable@example.com'),
('Express Delivery', 'express@example.com'),
('Timely Transport', 'timely@example.com'),
('Secure Shipping', 'secure@example.com'),
('Prime Courier', 'prime@example.com'),
('Swift Delivery', 'swift@example.com'),
('Rapid Transport', 'rapid@example.com'),
('Economy Delivery', 'economy@example.com');

--supplier
INSERT INTO supplier (name, contact_info) VALUES
('Fresh Farms', 'contact@freshfarms.com'),
('Organic Valley', 'info@organicvalley.com'),
('Greenhouse Goods', 'support@greenhousegoods.com'),
('Fruitful Fields', 'sales@fruitfulfields.com'),
('Veggie Delight', 'service@veggiedelight.com');
 
--product
INSERT INTO product (name, description, price, quantity_available, supplier_id, "size_of_product(in m³)") VALUES
('Apple', 'Fresh Red Apple', 0.5, 100, 1, 10),
('Banana', 'Ripe Yellow Banana', 0.3, 150, 1, 15),
('Carrot', 'Organic Carrot', 0.2, 200, 2, 8),
('Tomato', 'Fresh Tomato', 0.4, 180, 2, 12),
('Potato', 'Russet Potato', 0.25, 220, 3, 20),
('Lettuce', 'Green Leaf Lettuce', 0.7, 80, 3, 18),
('Broccoli', 'Fresh Broccoli', 0.6, 90, 4, 14),
('Orange', 'Juicy Orange', 0.5, 140, 4, 22),
('Strawberry', 'Sweet Strawberry', 1.0, 60, 5, 5),
('Grapes', 'Seedless Grapes', 0.8, 110, 5, 7),
('Onion', 'Yellow Onion', 0.3, 170, 1, 12),
('Cucumber', 'Fresh Cucumber', 0.4, 130, 1, 11),
('Mango', 'Sweet Mango', 1.5, 50, 2, 25),
('Blueberry', 'Fresh Blueberry', 2.0, 40, 2, 4),
('Pineapple', 'Juicy Pineapple', 1.8, 30, 3, 30),
('Zucchini', 'Organic Zucchini', 0.5, 120, 3, 15),
('Peach', 'Sweet Peach', 1.0, 70, 4, 13),
('Plum', 'Ripe Plum', 0.8, 90, 4, 9),
('Spinach', 'Fresh Spinach', 0.6, 100, 5, 8),
('Bell Pepper', 'Red Bell Pepper', 0.7, 80, 5, 18);


--warehouse_location
INSERT INTO warehouse_location (name, location, warehouse_manager) VALUES
('Central Warehouse', '123 Warehouse St', 'John Warehouse'),
('East Warehouse', '456 Storage Ave', 'Jane Storage'),
('West Warehouse', '789 Depot Blvd', 'Alice Depot'),
('North Warehouse', '101 Facility Ln', 'Bob Facility'),
('South Warehouse', '202 Logistics Dr', 'Carol Logistics');


--storage_location
INSERT INTO storage_location (name, "capacity(in m³)" , "occupied_space(in m³)" , warehouse_id) VALUES
('A1', 5000, 2260, 1),
('A2', 5000, 2260, 1),
('B1', 5000, 1755, 2),
('B2', 5000, 1755, 2),
('C1', 5000, 2340, 3),
('C2', 5000, 2340, 3),
('D1', 5000, 1230, 4),
('D2', 5000, 1230, 4),
('E1', 5000, 2620, 5),
('E2', 5000, 2550, 5);
 
 
--inventory_transactions
INSERT INTO inventory_transactions (transaction_type, transaction_date, notes) VALUES 
('IN', '2024-06-01 00:00:00', 'Initial stock of Apple'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Banana'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Carrot'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Tomato'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Potato'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Lettuce'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Broccoli'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Orange'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Strawberry'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Grapes'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Onion'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Cucumber'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Mango'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Blueberry'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Pineapple'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Zucchini'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Peach'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Plum'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Spinach'),
('IN', '2024-06-01 00:00:00', 'Initial stock of Bell Pepper');
 
 
--product_storage_location
INSERT INTO product_storage_location (quantity, storage_location_id, product_id, inventory_transaction_id) VALUES 
(50, 1, 1, 1),
(50, 2, 1, 1),
(75, 3, 2, 2),
(75, 4, 2, 2),
(100, 5, 3, 3),
(100, 6, 3, 3),
(90, 7, 4, 4),
(90, 8, 4, 4),
(110, 9, 5, 5),
(110, 10, 5, 5),
(80, 1, 6, 6),
(80, 2, 6, 6),
(45, 3, 7, 7),
(45, 4, 7, 7),
(70, 5, 8, 8),
(70, 6, 8, 8),
(30, 7, 9, 9),
(30, 8, 9, 9),
(60, 9, 10, 10),
(50, 10, 10, 10);