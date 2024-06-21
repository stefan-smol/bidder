-- If Entry Scripts do not work, please run these
CREATE DATABASE IF NOT EXISTS user_service_db;
CREATE DATABASE IF NOT EXISTS catalogue_service_db;
CREATE DATABASE IF NOT EXISTS auction_processing_service_db;
CREATE DATABASE IF NOT EXISTS payment_processing_service_db;

-- Attempt to create a user
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'pass';

-- Grant permissions to the user
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

-- Apply changes
FLUSH PRIVILEGES;