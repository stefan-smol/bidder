-- Create the catalogue_service_db database
CREATE DATABASE IF NOT EXISTS catalogue_service_db;
USE catalogue_service_db;
--CREATE DATABASE IF NOT EXISTS payment_processing_service_db;
--CREATE DATABASE IF NOT EXISTS user_service_db;


-- Create the Item table
--CREATE TABLE IF NOT EXISTS item (
--    id BIGINT AUTO_INCREMENT PRIMARY KEY,
--    name VARCHAR(255) NOT NULL,
--    description TEXT NOT NULL,
--    msrp DOUBLE,
--    category ENUM('APPAREL', 'JEWELRY', 'ELECTRONICS', 'COLLECTABLES', 'FURNITURE', 'HEALTH', 'COSMETICS', 'TOOLS', 'AUTOMOTIVE', 'OTHER') NOT NULL,
--    brand VARCHAR(255),
--    itemCondition ENUM('NEW', 'USED', 'REFURBISHED') NOT NULL
--);

-- Create the Auction table with adjusted timings to precede the bids
--CREATE TABLE IF NOT EXISTS auction (
--    id BIGINT AUTO_INCREMENT PRIMARY KEY,
--    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--    auctionType ENUM('FORWARD', 'DUTCH') NOT NULL,
--    startTime TIMESTAMP NOT NULL,
--    expiryTime TIMESTAMP NOT NULL,
--    status ENUM('SCHEDULED', 'RUNNING', 'EXPIRED') NOT NULL,
--    startingPrice DOUBLE NOT NULL,
--    sellerUsername VARCHAR(255) NOT NULL,
--    item_id BIGINT UNIQUE,
--    CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES item(id)
--);

-- Insert Item mock data
--INSERT INTO item (name, description, msrp, category, brand, item_condition) VALUES
--('Antique Vase', 'An exquisite antique vase ', 300.00, 'COLLECTABLES', 'AntiquesCo', 'USED'),
--('High-End Laptop', 'A top-of-the-line laptop.', 2000.00, 'ELECTRONICS', 'Techie', 'NEW'),
--('Designer Handbag', 'Luxurious handbag', 1500.00, 'APPAREL', 'LuxBrand', 'NEW');
-- Insert Auction mock data
--INSERT INTO auction (auction_type, start_time, expiry_time, status, starting_price, seller_username, item_id) VALUES
--('FORWARD', '2024-01-30 10:00:00', '2024-02-01 10:00:00', 'EXPIRED', 150.00, 'admin1', 1),
--('FORWARD', '2024-01-31 10:00:00', '2024-02-02 10:00:00', 'EXPIRED', 200.00, 'admin1', 2),
--('DUTCH', '2024-02-01 10:00:00', '2024-02-03 10:00:00', 'EXPIRED', 250.00, 'admin1', 3);