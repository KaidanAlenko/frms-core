CREATE DATABASE frmsbackend;
CREATE USER frmsbackend WITH PASSWORD 'frmsbackend';
GRANT ALL PRIVILEGES ON DATABASE frmsbackend to frmsbackend;

CREATE DATABASE frmsbackendutest;
CREATE USER frmsbackendutest WITH PASSWORD 'frmsbackendutest';
GRANT ALL PRIVILEGES ON DATABASE frmsbackendutest to frmsbackendutest;