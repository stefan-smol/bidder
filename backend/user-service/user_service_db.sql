-- Create the database
CREATE DATABASE IF NOT EXISTS user_service_db;
USE user_service_db;


-- -- Create the ShippingAddress table
-- CREATE TABLE IF NOT EXISTS shipping_address (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     country VARCHAR(255) NOT NULL,
--     city VARCHAR(255) NOT NULL,
--     postal_code VARCHAR(20) NOT NULL,
--     street_address VARCHAR(255) NOT NULL,
--     street_number VARCHAR(20) NOT NULL
-- );
--
-- -- Create the User table
-- CREATE TABLE IF NOT EXISTS user (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     username VARCHAR(255) NOT NULL UNIQUE,
--     email VARCHAR(255) NOT NULL UNIQUE,
--     password VARCHAR(255) NOT NULL,
--     first_name VARCHAR(255) NOT NULL,
--     last_name VARCHAR(255) NOT NULL,
--     role ENUM('USER', 'ADMIN') NOT NULL,
--     shipping_id BIGINT,
--     CONSTRAINT fk_shipping_address FOREIGN KEY (shipping_id) REFERENCES shipping_address(id)
-- );
--
--
-- -- Insert mock data into ShippingAddress table
-- INSERT INTO shipping_address (country, city, postal_code, street_address, street_number)
-- VALUES
-- ('USA', 'New York', '10001', '123 Main St', '1'),
-- ('UK', 'London', 'SW1A 1AA', '456 Park Ave', '2');
-- -- Insert mock data into User table
--
-- INSERT INTO user (username, email, password, first_name, last_name, role, shipping_id)
-- VALUES
-- ('user1', 'user1@example.com', 'password1', 'John', 'Doe', 'USER', 1),
-- ('admin1', 'admin1@example.com', 'adminpassword1', 'Jane', 'Smith', 'ADMIN', 2);