DROP TABLE IF EXISTS contacts CASCADE;
CREATE TABLE contacts
(
    contact_id      SERIAL,
    portfolio_id    INT NOT NULL,
    contact_type   VARCHAR(50) NOT NULL,
    contact_value  VARCHAR(255) NOT NULL,
    display_order  INT DEFAULT 0,
    is_visible     BOOLEAN DEFAULT TRUE,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (contact_id),
    CONSTRAINT fk_contacts_portfolio_id
        FOREIGN KEY (portfolio_id)
        REFERENCES portfolios (portfolio_id)
        ON DELETE CASCADE
);