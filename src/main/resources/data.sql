DROP TABLE IF EXISTS messages;

CREATE TABLE messages (
  id INT PRIMARY KEY,
  body VARCHAR(250) NOT NULL,
  destination VARCHAR(250) NOT NULL,
  createdAt TIMESTAMP DEFAULT NULL
);

INSERT INTO messages (id, body, destination, createdAt) VALUES
  (1, 'Привет, сосед!', 'test.queue1', '2021-05-01T10:21:59'),
  (2, 'Пока, сосед!', 'test.queue2', '2021-05-17T15:21:59'),
  (3, 'Hello, world!', 'test.queue1', '2021-05-10T15:21:59');