CREATE USER reconciliation_user WITH PASSWORD 'password';
CREATE DATABASE reconciliation_db;
GRANT ALL PRIVILEGES ON DATABASE reconciliation_db TO reconciliation_user;
\c reconciliation_db
GRANT ALL ON SCHEMA public TO reconciliation_user; 