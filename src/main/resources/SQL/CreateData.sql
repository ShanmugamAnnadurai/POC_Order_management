create table customer (customer_id bigint generated by default as identity, address varchar(255) not null, contact_info varchar(255) not null, name varchar(255) not null, primary key (customer_id));
create table delivery_partner (delivery_partner_id bigint generated by default as identity, contact_info varchar(255) not null, name varchar(255) not null, primary key (delivery_partner_id));
create table inventory_transactions (inventory_transaction_id bigint generated by default as identity, transaction_date timestamp(6) with time zone, notes varchar(255), transaction_type varchar(255), primary key (inventory_transaction_id));
create table product (price float(53), quantity_available integer, product_id bigint generated by default as identity, description varchar(255), name varchar(255), primary key (product_id));
create table product_storage_location (quantity integer, inventory_transaction_id bigint, product_id bigint, product_storage_location_id bigint generated by default as identity, storage_location_id bigint, primary key (product_storage_location_id));
create table purchase_order (total_price float(53), customer_id bigint, delivery_date timestamp(6) with time zone, order_date timestamp(6) with time zone not null, purchase_order_id bigint generated by default as identity, payment_method varchar(255), status varchar(255), primary key (purchase_order_id));
create table purchase_order_item_details (price float(53), quantity integer, order_details_id bigint generated by default as identity, product_id bigint, purchase_order_id bigint, primary key (order_details_id));
create table shipment (arrival_date timestamp(6) with time zone, delivery_partner_id bigint, purchase_order_id bigint unique, shipment_date timestamp(6) with time zone not null, shipment_id bigint generated by default as identity, status varchar(255), primary key (shipment_id));
create table storage_location (capacity integer, occupied_space integer, storage_location_id bigint generated by default as identity, warehouse_id bigint, name varchar(255), primary key (storage_location_id));
create table warehouse_location (warehouse_location_id bigint generated by default as identity, location varchar(255) not null, name varchar(255) not null, warehouse_manager varchar(255) not null, primary key (warehouse_location_id));


alter table if exists product_storage_location add constraint FKed1tjh778qu82589meufdoyy7 foreign key (inventory_transaction_id) references inventory_transactions;
alter table if exists product_storage_location add constraint FK6vfiio8tqf1l73udkwlj4k0wr foreign key (product_id) references product;
alter table if exists product_storage_location add constraint FKnuvkrkcpnmcj7hgu4sqiqh0me foreign key (storage_location_id) references storage_location;
alter table if exists purchase_order add constraint FK158vbkwgyf5r6ogk9nkugqv2c foreign key (customer_id) references customer;
alter table if exists purchase_order_item_details add constraint FK1upfkouos7x51mo2aqlawyy2l foreign key (product_id) references product;
alter table if exists purchase_order_item_details add constraint FKfuqbyis2n1bk8x70egvocd4ah foreign key (purchase_order_id) references purchase_order;
alter table if exists shipment add constraint FKo713iemwenanms28ia5775hvl foreign key (delivery_partner_id) references delivery_partner;
alter table if exists shipment add constraint FKbk23d29y65f0ana2bi4ja7yak foreign key (purchase_order_id) references purchase_order;
alter table if exists storage_location add constraint FK2d0fg15tgkegmy168jyfu723m foreign key (warehouse_id) references warehouse_location;