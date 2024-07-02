
DELETE FROM product_storage_location WHERE storage_location_id = 2;


DELETE FROM storage_location WHERE storage_location_id = 2;


DELETE FROM purchase_order_item_details WHERE purchase_order_id IN (SELECT purchase_order_id FROM purchase_order WHERE customer_id = 1);
DELETE FROM shipment WHERE purchase_order_id IN (SELECT purchase_order_id FROM purchase_order WHERE customer_id = 1);
DELETE FROM purchase_order WHERE customer_id = 1;
DELETE FROM delivery_partner WHERE delivery_partner_id = 1;
DELETE FROM product WHERE product_id = 1;
DELETE FROM customer WHERE customer_id = 1;


-- Delete child records first
DELETE FROM purchase_order_item_details;
DELETE FROM shipment;
DELETE FROM product_storage_location;
DELETE FROM inventory_transactions;

-- Then delete from the parent tables
DELETE FROM purchase_order;
DELETE FROM customer;
DELETE FROM delivery_partner;
DELETE FROM product;
DELETE FROM storage_location;
DELETE FROM warehouse_location;

