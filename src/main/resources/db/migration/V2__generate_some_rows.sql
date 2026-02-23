INSERT INTO users (user_image, username, email, user_password, user_role)
VALUES
-- Администратор
(NULL, 'admin', 'admin@portfolio.com', '$2a$12$5Y5p7V9p0O3r8Q2sT6uBcO.9vW1xY3zA4B5C6D7E8F9G0H1I2J3K4L5M6N7O8P',
 'ADMIN'),

-- Пользователи
(NULL, 'john_doe', 'john.doe@email.com', '$2a$12$1A2B3C4D5E6F7G8H9I0J.K1L2M3N4O5P6Q7R8S9T0U1V2W3X4Y5Z6a7b8c', 'USER'),
(NULL, 'alice_smith', 'alice.smith@email.com', '$2a$12$d9e8f7g6h5i4j3k2l1m0n.o9p8q7r6s5t4u3v2w1x0y1z2a3b4c5d6e7f8',
 'USER'),
(NULL, 'bob_johnson', 'bob.j@email.com', '$2a$12$g5h6i7j8k9l0m1n2o3p4q.r5s6t7u8v9w0x1y2z3a4b5c6d7e8f9g0h1i', 'USER'),
(NULL, 'eva_davis', 'eva.davis@work.com', '$2a$12$j2k3l4m5n6o7p8q9r0s1t.u2v3w4x5y6z7a8b9c0d1e2f3g4h5i6j7k8l', 'USER');

-- Портфолио
INSERT INTO portfolios (user_id, views_count, created_at, updated_at)
VALUES (1, 1500, '2024-01-15 10:30:00', '2024-03-20 14:45:00'),
       (2, 450, '2024-02-10 09:15:00', '2024-03-25 11:20:00'),
       (3, 890, '2024-01-20 14:20:00', '2024-04-01 16:30:00'),
       (4, 210, '2024-03-05 11:45:00', '2024-03-28 10:15:00'),
       (5, 670, '2024-02-28 16:10:00', '2024-04-05 09:40:00');

-- Теги
INSERT INTO tags (tag_name, tag_type)
VALUES ('Photography', 'DOMAIN'),
       ('Design', 'DOMAIN'),
       ('Web Development', 'DOMAIN'),

       ('Portrait', 'CATEGORY'),
       ('Landscape', 'CATEGORY'),
       ('UI/UX', 'CATEGORY'),
       ('Landing Page', 'CATEGORY'),

       ('Photoshop', 'TOOL'),
       ('Figma', 'TOOL'),
       ('Lightroom', 'TOOL'),
       ('HTML', 'TECHNOLOGY'),
       ('CSS', 'TECHNOLOGY'),
       ('JavaScript', 'TECHNOLOGY');

-- Проекты
INSERT INTO projects (portfolio_id,
                      project_preview,
                      project_title,
                      full_description,
                      short_description,
                      project_link)
VALUES (1,
        NULL,
        'Portrait Photography Series',
        'A professional portrait photography project focused on studio lighting and emotional expression.',
        'Studio portrait photography',
        'https://example.com/portrait-project'),
       (1,
        NULL,
        'Landscape Photo Collection',
        'A collection of landscape photographs captured in natural lighting conditions.',
        'Nature and landscape photography',
        NULL),
       (2,
        NULL,
        'Landing Page Design',
        'UI/UX design of a modern landing page created using Figma.',
        'Landing page UI/UX design',
        'https://figma.com/example');

-- Project 1: Portrait Photography
INSERT INTO tags_in_project (project_id, tag_id)
VALUES (1, 1), -- Photography
       (1, 4), -- Portrait
       (1, 10);
-- Lightroom

-- Project 2: Landscape Photography
INSERT INTO tags_in_project (project_id, tag_id)
VALUES (2, 1), -- Photography
       (2, 5), -- Landscape
       (2, 10);
-- Lightroom

-- Project 3: Landing Page Design
INSERT INTO tags_in_project (project_id, tag_id)
VALUES (3, 2), -- Design
       (3, 6), -- UI/UX
       (3, 9);
-- Figma

-- Изображения в проекте
INSERT INTO project_images (project_id, image_data, image_format)
VALUES (1,
        decode('89504E470D0A', 'hex'),
        'png'),
       (1,
        decode('FFD8FFE000104A', 'hex'),
        'jpg'),
       (3,
        decode('89504E470D0A', 'hex'),
        'png');