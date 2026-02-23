DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users
(
    user_id       SERIAL,
    user_image    BYTEA,
    username      VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    user_password VARCHAR(255) NOT NULL,
    user_role     VARCHAR(10)  NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);

DROP TABLE IF EXISTS portfolios CASCADE;
CREATE TABLE portfolios
(
    portfolio_id SERIAL,
    user_id      INT NOT NULL UNIQUE, -- 1 to 1
    views_count  INT,                 --DEFAULT ,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP,
    PRIMARY KEY (portfolio_id),

    CONSTRAINT fk_portfolio_user_id
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS tags CASCADE;
CREATE TABLE tags
(
    tag_id   SERIAL,
    tag_name VARCHAR(255) NOT NULL,
    tag_type VARCHAR(255) NOT NULL,
    PRIMARY KEY (tag_id)
);

DROP TABLE IF EXISTS projects CASCADE;
CREATE TABLE projects
(
    project_id        SERIAL,
    portfolio_id      INT NOT NULL,
    project_preview   BYTEA,
    project_title     VARCHAR(255),
    full_description  TEXT,
    short_description TEXT,
    project_link      VARCHAR(255),
    is_visible        BOOLEAN   DEFAULT TRUE,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (project_id),
    FOREIGN KEY (portfolio_id) REFERENCES portfolios (portfolio_id)
);

DROP TABLE IF EXISTS tags_in_project CASCADE;
CREATE TABLE tags_in_project
(
    project_id INT NOT NULL,
    tag_id     INT NOT NULL,
    PRIMARY KEY (project_id, tag_id),
    FOREIGN KEY (project_id) REFERENCES projects (project_id),
    FOREIGN KEY (tag_id) REFERENCES tags (tag_id)
);

DROP TABLE IF EXISTS project_images CASCADE;
CREATE TABLE project_images
(
    image_id     SERIAL,
    project_id   INT         NOT NULL,
    image_data   BYTEA       NOT NULL,
    image_format VARCHAR(63) NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (image_id),
    FOREIGN KEY (project_id) REFERENCES projects (project_id)
);


