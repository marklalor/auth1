CREATE DATABASE testDB;

CREATE TABLE testDB.family (
  member_id INT NOT NULL,
  name      VARCHAR(50),
  relation  VARCHAR(50)
);

INSERT INTO testDB.family
VALUES (1, 'Brian', 'Brother'),
  (2, 'Mark', 'Brother'),
  (3, 'Yidi', 'Brother');
