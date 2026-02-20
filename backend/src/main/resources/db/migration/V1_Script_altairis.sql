-- MariaDB DDL — Altairis Backoffice MVP (Final)

DROP TABLE IF EXISTS booking_nights;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS inventory_days;
DROP TABLE IF EXISTS room_types;
DROP TABLE IF EXISTS hotels;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS countries;

CREATE TABLE countries (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           iso2 CHAR(2) NOT NULL,
                           name VARCHAR(120) NOT NULL,
                           status VARCHAR(1) NOT NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           created_user VARCHAR(100) NOT NULL,
                           updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                           updated_user VARCHAR(100),
                           PRIMARY KEY (id),
                           UNIQUE KEY uk_countries_iso2 (iso2),
                           KEY idx_countries_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE cities (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        country_id BIGINT NOT NULL,
                        name VARCHAR(160) NOT NULL,
                        status VARCHAR(1) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        created_user VARCHAR(100) NOT NULL,
                        updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                        updated_user VARCHAR(100),
                        PRIMARY KEY (id),
                        CONSTRAINT fk_cities_country FOREIGN KEY (country_id) REFERENCES countries(id),
                        UNIQUE KEY uk_cities_country_name (country_id, name),
                        KEY idx_cities_country (country_id),
                        KEY idx_cities_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE hotels (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        code VARCHAR(32) NOT NULL,
                        name VARCHAR(200) NOT NULL,
                        city_id BIGINT NOT NULL,
                        address VARCHAR(240),
                        stars TINYINT,
                        latitude DECIMAL(9,6),
                        longitude DECIMAL(9,6),
                        status VARCHAR(1) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        created_user VARCHAR(100) NOT NULL,
                        updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                        updated_user VARCHAR(100),
                        PRIMARY KEY (id),
                        CONSTRAINT fk_hotels_city FOREIGN KEY (city_id) REFERENCES cities(id),
                        UNIQUE KEY uk_hotels_code (code),
                        KEY idx_hotels_city (city_id),
                        KEY idx_hotels_name (name),
                        KEY idx_hotels_geo (latitude, longitude)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE room_types (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            hotel_id BIGINT NOT NULL,
                            code VARCHAR(32) NOT NULL,
                            name VARCHAR(160) NOT NULL,
                            capacity_adults TINYINT NOT NULL DEFAULT 2,
                            capacity_children TINYINT NOT NULL DEFAULT 0,
                            status VARCHAR(1) NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            created_user VARCHAR(100) NOT NULL,
                            updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                            updated_user VARCHAR(100),
                            PRIMARY KEY (id),
                            CONSTRAINT fk_room_types_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(id),
                            UNIQUE KEY uk_room_types_hotel_code (hotel_id, code),
                            KEY idx_room_types_hotel (hotel_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE inventory_days (
                                id BIGINT NOT NULL AUTO_INCREMENT,
                                room_type_id BIGINT NOT NULL,
                                date DATE NOT NULL,
                                total_units INT NOT NULL,
                                available_units INT NOT NULL,
                                price DECIMAL(10,2),
                                currency CHAR(3) NOT NULL DEFAULT 'EUR',
                                status VARCHAR(1) NOT NULL,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                created_user VARCHAR(100) NOT NULL,
                                updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                updated_user VARCHAR(100),
                                PRIMARY KEY (id),
                                CONSTRAINT fk_inventory_days_room_type FOREIGN KEY (room_type_id) REFERENCES room_types(id),
                                CONSTRAINT ck_inventory_units CHECK (total_units >= 0 AND available_units >= 0 AND available_units <= total_units),
                                UNIQUE KEY uk_inventory_days_room_date (room_type_id, date),
                                KEY idx_inventory_days_date (date),
                                KEY idx_inventory_days_room (room_type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE bookings (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          booking_ref VARCHAR(40) NOT NULL,
                          hotel_id BIGINT NOT NULL,
                          room_type_id BIGINT NOT NULL,
                          check_in DATE NOT NULL,
                          check_out DATE NOT NULL,
                          rooms INT NOT NULL DEFAULT 1,
                          customer_name VARCHAR(160) NOT NULL,
                          notes VARCHAR(500),
                          status VARCHAR(1) NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          created_user VARCHAR(100) NOT NULL,
                          updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                          updated_user VARCHAR(100),
                          PRIMARY KEY (id),
                          CONSTRAINT fk_bookings_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(id),
                          CONSTRAINT fk_bookings_room_type FOREIGN KEY (room_type_id) REFERENCES room_types(id),
                          CONSTRAINT ck_bookings_dates CHECK (check_out > check_in),
                          CONSTRAINT ck_bookings_rooms CHECK (rooms > 0),
                          UNIQUE KEY uk_bookings_ref (booking_ref),
                          KEY idx_bookings_hotel_checkin (hotel_id, check_in),
                          KEY idx_bookings_room_checkin (room_type_id, check_in)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE booking_nights (
                                id BIGINT NOT NULL AUTO_INCREMENT,
                                booking_id BIGINT NOT NULL,
                                room_type_id BIGINT NOT NULL,
                                date DATE NOT NULL,
                                rooms INT NOT NULL,
                                PRIMARY KEY (id),
                                CONSTRAINT fk_booking_nights_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
                                CONSTRAINT fk_booking_nights_room_type FOREIGN KEY (room_type_id) REFERENCES room_types(id),
                                CONSTRAINT ck_booking_nights_rooms CHECK (rooms > 0),
                                UNIQUE KEY uk_booking_nights_booking_date (booking_id, date),
                                KEY idx_booking_nights_room_date (room_type_id, date),
                                KEY idx_booking_nights_date (date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
