CREATE SCHEMA IF NOT EXISTS tennisScoreboard;
SET SCHEMA tennisScoreboard;

CREATE TABLE IF NOT EXISTS Players
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    CONSTRAINT name_unique UNIQUE (Name)
);

INSERT INTO Players (name)
VALUES
    ('Ivan'),
    ('Maxim'),
    ('Egor'),
    ('Hope'),
    ('Believe'),
    ('Love');

CREATE TABLE IF NOT EXISTS Matches
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pLayer1 BIGINT NOT NULL,
    pLayer2 BIGINT NOT NULL,
    winner BIGINT NOT NULL,
    FOREIGN KEY (pLayer1) REFERENCES Players (id) ON DELETE RESTRICT,
    FOREIGN KEY (pLayer2) REFERENCES Players (id) ON DELETE RESTRICT,
    FOREIGN KEY (winner) REFERENCES Players (id) ON DELETE RESTRICT
);

INSERT INTO Matches (pLayer1, pLayer2, winner)
VALUES
    (1,2, 1),
    (2, 3, 2),
    (3, 4, 3),
    (5, 4, 4),
    (4, 6, 6);