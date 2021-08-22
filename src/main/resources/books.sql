CREATE TABLE books
(
    id              VARCHAR(32) PRIMARY KEY,
    title           VARCHAR(200),
    description     TEXT,
    imageUrl        VARCHAR(200),
    category        VARCHAR(50),
    tax             NUMERIC(9, 2),
    price           NUMERIC(9, 2),
    stock           INTEGER,
    numberOfReviews INTEGER
);