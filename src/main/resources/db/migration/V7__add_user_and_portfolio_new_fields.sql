ALTER TABLE users
ADD first_name VARCHAR(100),
ADD last_name VARCHAR(100);

ALTER TABLE portfolios
ADD bio TEXT,
ADD specialization VARCHAR(255),
ADD experience_years INTEGER;